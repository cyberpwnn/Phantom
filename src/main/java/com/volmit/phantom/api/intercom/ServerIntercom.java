package com.volmit.phantom.api.intercom;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.util.concurrent.ExecutorUtil;

public class ServerIntercom extends Thread implements IntercomCoordinator
{
	private final String accessToken;
	private final GList<CoordinatedIntercom> channels;
	private final ServerSocket socket;
	private ExecutorService svc;
	final GList<CoordinatorMonitor> monitors;
	private final GMap<Class<? extends Serializable>, IntercomHandler<?, ?>> handlers;
	private boolean active;
	private int conc;

	public ServerIntercom(String name, int concurrency, int port, String accessToken) throws IOException
	{
		conc = concurrency;
		handlers = new GMap<>();
		monitors = new GList<>();
		this.accessToken = accessToken;
		channels = new GList<>();
		setName(name + " Intercom Conductor");
		active = false;
		socket = new ServerSocket(port);
		socket.setSoTimeout(1000);
		svc = ExecutorUtil.createForkJoinPool(name + " Intercom Channel ", true, conc, new UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				e.printStackTrace();
			}
		});
	}

	@Override
	public boolean hasChannel(String server)
	{
		return getChannel(server) != null;
	}

	@Override
	public CoordinatedIntercom getChannel(String server)
	{
		for(CoordinatedIntercom i : channels)
		{
			if(i.getServerName().equals(server))
			{
				return i;
			}
		}

		return null;
	}

	public ServerIntercom begin()
	{
		start();
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable, X extends Serializable> ServerIntercom accept(Class<? extends T> c, IntercomServerHelper<T, X> h)
	{
		try
		{
			Serializable dummy = c.getConstructor().newInstance();
			return formaccept(c, h, new IntercomMessage<T>((T) dummy));
		}

		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}

		return this;
	}

	private <T extends Serializable, X extends IntercomMessage<T>> ServerIntercom formaccept(Class<? extends Serializable> c, IntercomServerHelper<T, ? extends Serializable> h, IntercomMessage<T> in)
	{
		return doaccept(c, (i) -> new IntercomMessage<>(h.handle((T) i.getMessage())), in);
	}

	private <T extends Serializable> ServerIntercom doaccept(Class<? extends Serializable> c, IntercomServerHandle<IntercomMessage<T>, IntercomMessage<? extends Serializable>> h, IntercomMessage<T> in)
	{
		handlers.put(c, new IntercomHandler<IntercomMessage<T>, IntercomMessage<? extends Serializable>>()
		{
			@Override
			public IntercomMessage<? extends Serializable> handle(IntercomMessage<T> in)
			{
				return h.handle(in);
			}

			@Override
			public IntercomMessage<T> createContainer()
			{
				return in;
			}
		});

		return this;
	}

	@Override
	public void shutdown()
	{
		active = false;
	}

	@Override
	public void run()
	{
		active = true;

		while(active)
		{
			try
			{
				ServerIntercomChannel sc = new ServerIntercomChannel(this, socket.accept(), accessToken, handlers);
				channels.add(sc);
				for(CoordinatorMonitor i : monitors)
				{
					i.onConnected(sc);

					if(sc.isDead())
					{
						i.onDisconnected(sc);
					}

					else
					{
						i.onAuthenticated(sc);
					}
				}
			}

			catch(SocketTimeoutException e)
			{

			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}

			try
			{
				Thread.sleep(5);

				for(CoordinatedIntercom i : channels.copy())
				{
					if(i.isDead() || i.hasWorkToDo())
					{
						svc.execute(new Runnable()
						{
							@Override
							public void run()
							{
								if(i.isDead())
								{
									for(CoordinatorMonitor j : monitors)
									{
										j.onDisconnected(i);
									}

									channels.remove(i);
									return;
								}

								if(i.hasWorkToDo())
								{
									svc.execute(new Runnable()
									{
										@Override
										public void run()
										{
											i.doWork();
										}
									});
								}
							}
						});
					}
				}
			}

			catch(Throwable e)
			{

			}
		}

		try
		{
			socket.close();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public IntercomCoordinator addMonitor(CoordinatorMonitor monitor)
	{
		monitors.add(monitor);
		return this;
	}

	@Override
	public IntercomCoordinator removeMonitor(CoordinatorMonitor monitor)
	{
		monitors.remove(monitor);
		return this;
	}

	@Override
	public GList<CoordinatedIntercom> getChannels()
	{
		return channels.copy();
	}
}

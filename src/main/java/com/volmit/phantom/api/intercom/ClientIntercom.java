package com.volmit.phantom.api.intercom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import com.volmit.phantom.api.lang.Callback;
import com.volmit.phantom.api.lang.FinalBoolean;
import com.volmit.phantom.api.lang.GBiset;
import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.lang.V;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.util.concurrent.ExecutorUtil;
import com.volmit.phantom.util.queue.PhantomQueue;
import com.volmit.phantom.util.queue.Queue;

public class ClientIntercom extends Thread implements IntercomChannel
{
	private final GMap<Class<? extends Serializable>, IntercomHandler<?, ?>> handlers;
	private final String accessToken;
	private boolean active;
	private Socket socket;
	private final GList<ChannelMonitor> monitors;
	private final String address;
	private String server;
	private final int port;
	private final GMap<UUID, Callback<Serializable>> callbackQueue;
	private final ExecutorService svc;
	private final GMap<Class<? extends Serializable>, InboxExecutor<Serializable>> inboxExecutors;
	private final Queue<Serializable> inbox;
	private final Queue<GBiset<Serializable, Callback<Serializable>>> queue;

	public ClientIntercom(String server, String name, String address, int port, String token)
	{
		setName(name + " Intercom Connection");
		monitors = new GList<>();
		handlers = new GMap<>();
		callbackQueue = new GMap<>();
		inbox = new PhantomQueue<>();
		queue = new PhantomQueue<>();
		inboxExecutors = new GMap<>();
		this.server = server;
		this.accessToken = token;
		this.address = address;
		this.port = port;
		active = false;

		svc = ExecutorUtil.createForkJoinPool(name + " Intercom Uplink ", true, 8, new UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				e.printStackTrace();
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> IntercomChannel addInboxExecutor(Class<? extends T> s, InboxExecutor<T> t)
	{
		inboxExecutors.put(s, (InboxExecutor<Serializable>) t);
		return this;
	}

	public Queue<Serializable> getInbox()
	{
		return inbox;
	}

	@Override
	public void run()
	{
		active = true;

		while(active)
		{
			try
			{
				reconnect();
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}

			try
			{
				Thread.sleep(1000);
			}

			catch(InterruptedException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	private void reconnect() throws UnknownHostException, IOException, InterruptedException
	{
		socket = new Socket();

		try
		{
			socket.connect(new InetSocketAddress(address, port), 1000);
		}

		catch(SocketTimeoutException e)
		{
			return;
		}

		FinalBoolean uploading = new FinalBoolean(false);
		FinalBoolean downloading = new FinalBoolean(false);
		DataInputStream din = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

		for(ChannelMonitor j : monitors)
		{
			j.onConnected();
		}

		if(din.read() == 0)
		{
			dos.writeUTF(server);
			dos.writeUTF(accessToken);
			dos.flush();

			if(din.read() != 1)
			{
				for(ChannelMonitor j : monitors)
				{
					j.onAuthenticationFailure();
					j.onDisconnected();
				}

				socket.close();
				return;
			}
		}

		for(ChannelMonitor j : monitors)
		{
			j.onAuthenticated();
		}

		while(socket.isConnected())
		{
			Thread.sleep(5);
			process(uploading, downloading, din, dos);
			processInbox();
		}
	}

	private void processInbox()
	{
		if(!inbox.hasNext())
		{
			return;
		}

		svc.execute(new Runnable()
		{
			@Override
			public void run()
			{
				while(inbox.hasNext())
				{
					Serializable s = inbox.next();

					if(inboxExecutors.containsKey(s.getClass()))
					{
						inboxExecutors.get(s.getClass()).onInboxMessageReceived(s);
					}
				}
			}
		});
	}

	private void process(FinalBoolean uploading, FinalBoolean downloading, DataInputStream din, DataOutputStream dos)
	{
		try
		{
			if(socket.isClosed())
			{
				return;
			}

			if(din.available() <= 0 && !queue.hasNext())
			{
				return;
			}

			if(!uploading.get())
			{
				uploading.set(true);
				svc.execute(() -> upload(din, dos, uploading));
			}

			if(!downloading.get())
			{
				downloading.set(true);
				svc.execute(() -> download(din, dos, downloading));
			}
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	protected void upload(DataInputStream din, DataOutputStream dos, FinalBoolean uploading)
	{
		while(queue.hasNext())
		{
			GBiset<Serializable, Callback<Serializable>> o = queue.next();

			if(o == null)
			{
				continue;
			}

			if(o.getA() == null || o.getB() == null)
			{
				continue;
			}

			try
			{
				up(o, din, dos);
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			dos.flush();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		uploading.set(false);
	}

	@SuppressWarnings("unchecked")
	protected void download(DataInputStream din, DataOutputStream dos, FinalBoolean downloading)
	{
		try
		{
			while(din.available() > 0)
			{
				try
				{
					Class<? extends Serializable> clazz = (Class<? extends Serializable>) Class.forName(din.readUTF());

					if(handlers.containsKey(clazz))
					{
						IntercomHandler<?, ?> ih = handlers.get(clazz);
						IntercomMessage<?> im = ih.createContainer();
						im.read(din);

						for(ChannelMonitor j : monitors)
						{
							j.onReceive(im.getMessage().getClass());
						}

						if(callbackQueue.containsKey(im.getPayload()))
						{
							callbackQueue.get(im.getPayload()).run(im.getMessage());
							callbackQueue.remove(im.getPayload());
						}

						else
						{
							IntercomMessage<?> ir = (IntercomMessage<?>) new V(ih).invoke("handle", im);
							dos.writeUTF(ir.getMessage().getClass().getCanonicalName());
							ir.asResponseTo(im);
							ir.write(dos);

							for(ChannelMonitor j : monitors)
							{
								j.onSend(ir.getMessage().getClass());
							}

							dos.flush();
						}
					}

					else
					{
						IntercomMessage<Serializable> im = new IntercomMessage<Serializable>();
						im.read(din);

						for(ChannelMonitor j : monitors)
						{
							j.onReceive(im.getMessage().getClass());
						}

						if(callbackQueue.containsKey(im.getPayload()))
						{
							callbackQueue.get(im.getPayload()).run(im.getMessage());
							callbackQueue.remove(im.getPayload());
						}

						else
						{
							inbox.queue(im.getMessage());
						}
					}
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		downloading.set(false);
	}

	private void up(GBiset<Serializable, Callback<Serializable>> a, DataInputStream din, DataOutputStream dos) throws IOException
	{
		IntercomMessage<Serializable> send = new IntercomMessage<>(server, a.getA());
		dos.writeUTF(a.getA().getClass().getCanonicalName());
		send.write(dos);
		callbackQueue.put(send.getPayload(), a.getB());

		for(ChannelMonitor j : monitors)
		{
			j.onSend(a.getA().getClass());
		}
	}

	public void shutdown()
	{
		active = false;
	}

	public ClientIntercom begin()
	{
		start();
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <TX extends Serializable, TD extends Serializable> IntercomChannel sendMessage(TX s, Callback<TD> t)
	{
		queue.queue(new GBiset<Serializable, Callback<Serializable>>(s, (Callback<Serializable>) t));
		return this;
	}

	@Override
	public IntercomChannel sendBlindly(Serializable s)
	{
		return sendMessage(s, new Callback<Serializable>()
		{
			@Override
			public void run(Serializable t)
			{

			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T sendNow(Serializable s, long ms)
	{
		long m = M.ms();
		FinalBoolean fb = new FinalBoolean(true);
		Serializable[] v = new Serializable[1];
		v[0] = null;

		sendMessage(s, (g) ->
		{
			v[0] = g;
			fb.set(false);
		});

		while(fb.get())
		{
			try
			{
				if(M.ms() - m > ms)
				{
					fb.set(false);
				}

				Thread.sleep(5);
			}

			catch(InterruptedException e)
			{
				e.printStackTrace();
				break;
			}
		}

		return (T) v[0];
	}

	@Override
	public void disconnect()
	{
		active = false;

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
	@SuppressWarnings("unchecked")
	public <T extends Serializable, X extends Serializable> IntercomChannel accept(Class<? extends T> c, IntercomServerHelper<T, X> h)
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

	private <T extends Serializable, X extends IntercomMessage<T>> IntercomChannel formaccept(Class<? extends Serializable> c, IntercomServerHelper<T, ? extends Serializable> h, IntercomMessage<T> in)
	{
		return doaccept(c, (i) -> new IntercomMessage<>(h.handle((T) i.getMessage())), in);
	}

	private <T extends Serializable> IntercomChannel doaccept(Class<? extends Serializable> c, IntercomServerHandle<IntercomMessage<T>, IntercomMessage<? extends Serializable>> h, IntercomMessage<T> in)
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
	public IntercomChannel addMonitor(ChannelMonitor monitor)
	{
		monitors.add(monitor);
		return this;
	}

	@Override
	public IntercomChannel removeMonitor(ChannelMonitor monitor)
	{
		monitors.remove(monitor);
		return this;
	}

}

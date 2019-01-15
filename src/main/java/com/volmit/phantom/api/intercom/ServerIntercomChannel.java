package com.volmit.phantom.api.intercom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.UUID;

import com.volmit.phantom.api.lang.Callback;
import com.volmit.phantom.api.lang.FinalBoolean;
import com.volmit.phantom.api.lang.GBiset;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.lang.V;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.util.queue.PhantomQueue;
import com.volmit.phantom.util.queue.Queue;

public class ServerIntercomChannel implements CoordinatedIntercom
{
	private boolean working;
	private final Socket socket;
	private final String accessToken;
	private final DataInputStream in;
	private final DataOutputStream out;
	private final GMap<UUID, Callback<Serializable>> callbacks;
	private final Queue<Serializable> blindQueue;
	private final Queue<GBiset<Serializable, Callback<Serializable>>> sendQueue;
	private final GMap<Class<? extends Serializable>, IntercomHandler<?, ?>> handlers;
	private String serverId;
	private ServerIntercom inter;
	private boolean dead;

	public ServerIntercomChannel(ServerIntercom serverIntercom, Socket socket, String accessToken, GMap<Class<? extends Serializable>, IntercomHandler<?, ?>> handlers) throws IOException
	{
		this.inter = serverIntercom;
		working = false;
		this.handlers = handlers;
		callbacks = new GMap<>();
		sendQueue = new PhantomQueue<>();
		blindQueue = new PhantomQueue<>();
		this.socket = socket;
		serverId = "Connecting";
		this.accessToken = accessToken;
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
		authenticate();
	}

	private void authenticate() throws IOException
	{
		out.write(0);
		out.flush();
		serverId = in.readUTF();

		if(!this.accessToken.equals(in.readUTF()))
		{
			out.write(-1);
			out.flush();
			die();
			return;
		}

		out.write(1);
		out.flush();
	}

	public boolean isDead()
	{
		return dead || socket.isClosed();
	}

	public boolean hasWorkToDo()
	{
		if(working)
		{
			return false;
		}

		try
		{
			return in.available() > 0 || blindQueue.hasNext() || sendQueue.hasNext();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public void die()
	{
		try
		{
			socket.close();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		dead = true;
	}

	public String getServerId()
	{
		return serverId;
	}

	public void doWork()
	{
		working = true;

		try
		{
			while(sendQueue.hasNext())
			{
				GBiset<Serializable, Callback<Serializable>> s = sendQueue.next();
				IntercomMessage<?> ir = new IntercomMessage<Serializable>(s.getA());
				callbacks.put(ir.getPayload(), s.getB());
				out.writeUTF(ir.getMessage().getClass().getCanonicalName());
				ir.write(out);

				for(CoordinatorMonitor j : inter.monitors)
				{
					j.onSend(this, s.getB().getClass());
				}
			}

			while(blindQueue.hasNext())
			{
				Serializable s = blindQueue.next();
				IntercomMessage<?> ir = new IntercomMessage<Serializable>(s);
				out.writeUTF(ir.getMessage().getClass().getCanonicalName());
				ir.write(out);

				for(CoordinatorMonitor j : inter.monitors)
				{
					j.onSend(this, s.getClass());
				}
			}

			while(in.available() > 0)
			{
				try
				{
					@SuppressWarnings("unchecked")
					Class<? extends Serializable> clazz = (Class<? extends Serializable>) Class.forName(in.readUTF());

					if(handlers.containsKey(clazz))
					{
						IntercomHandler<?, ?> ih = handlers.get(clazz);
						@SuppressWarnings("unchecked")
						IntercomMessage<Serializable> im = (IntercomMessage<Serializable>) ih.createContainer();
						im.read(in);

						for(CoordinatorMonitor j : inter.monitors)
						{
							j.onReceive(this, im.getMessage().getClass());
						}

						if(callbacks.containsKey(im.getPayload()))
						{
							callbacks.get(im.getPayload()).run(im.getMessage());
							callbacks.remove(im.getPayload());
						}

						else
						{
							@SuppressWarnings("unchecked")
							IntercomMessage<Serializable> ir = (IntercomMessage<Serializable>) new V(ih).invoke("handle", im);
							out.writeUTF(ir.getMessage().getClass().getCanonicalName());
							ir.asResponseTo(im);
							ir.write(out);

							for(CoordinatorMonitor j : inter.monitors)
							{
								j.onSend(this, ir.getMessage().getClass());
							}
						}
					}

					else
					{
						IntercomMessage<Serializable> im = new IntercomMessage<Serializable>();
						im.read(in);

						for(CoordinatorMonitor j : inter.monitors)
						{
							j.onReceive(this, im.getMessage().getClass());
						}

						if(callbacks.containsKey(im.getPayload()))
						{
							callbacks.get(im.getPayload()).run(im.getMessage());
							callbacks.remove(im.getPayload());
						}

						else
						{
							throw new Throwable("Unable to find handler for class: " + clazz.getCanonicalName());
						}
					}
				}

				catch(Throwable e)
				{
					try
					{
						in.skip(in.available());
					}

					catch(IOException e1)
					{
						e1.printStackTrace();
					}

					e.printStackTrace();
				}
			}

			out.flush();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		working = false;
	}

	@Override
	public CoordinatedIntercom sendBlindly(Serializable s)
	{
		sendMessage(s, new Callback<Serializable>()
		{
			@Override
			public void run(Serializable t)
			{

			}
		});

		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <TX extends Serializable, TD extends Serializable> CoordinatedIntercom sendMessage(TX s, Callback<TD> t)
	{
		sendQueue.queue(new GBiset<Serializable, Callback<Serializable>>(s, (Callback<Serializable>) t));
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T sendNow(Serializable s, long ms)
	{
		long m = M.ms();
		FinalBoolean fb = new FinalBoolean(true);
		Serializable[] v = new Serializable[1];
		v[0] = null;

		sendMessage(s, (Serializable g) ->
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
		die();
	}

	@Override
	public String getServerName()
	{
		return serverId;
	}
}

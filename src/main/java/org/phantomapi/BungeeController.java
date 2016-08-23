package org.phantomapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.network.ForwardedPluginMessage;
import org.phantomapi.network.PluginMessage;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.transmit.Transmission;
import org.phantomapi.transmit.Transmitter;
import org.phantomapi.util.C;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

@Ticked(200)
public class BungeeController extends Controller implements PluginMessageListener
{
	private DataCluster cc;
	private GList<Transmitter> transmitters;
	private GList<Transmission> queue;
	private GList<Transmission> responders;
	private Boolean connected;
	private String sname;
	
	public BungeeController(Controllable parentController)
	{
		super(parentController);
		
		cc = new DataCluster();
		responders = new GList<Transmission>();
		transmitters = new GList<Transmitter>();
		connected = false;
		sname = null;
		queue = new GList<Transmission>();
		
		getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(getPlugin(), "BungeeCord");
		getPlugin().getServer().getMessenger().registerIncomingPluginChannel(getPlugin(), "BungeeCord", this);
	}
	
	public void registerTransmitter(Transmitter t)
	{
		transmitters.add(t);
	}
	
	public void unregisterTransmitter(Transmitter t)
	{
		transmitters.remove(t);
	}
	
	public void onTick()
	{
		new TaskLater((int) (Math.random() * 10))
		{
			@Override
			public void run()
			{
				new PluginMessage(getPlugin(), "GetServers").send();
				
				if(cc.contains("servers"))
				{
					for(String i : cc.getStringList("servers"))
					{
						new PluginMessage(getPlugin(), "PlayerCount", i).send();
						new PluginMessage(getPlugin(), "PlayerList", i).send();
					}
					
					new PluginMessage(getPlugin(), "PlayerCount", "ALL").send();
					new PluginMessage(getPlugin(), "PlayerList", "ALL").send();
					new PluginMessage(getPlugin(), "GetServer").send();
				}
			}
		};
	}
	
	public boolean canFire(Transmission t)
	{
		return Phantom.instance().isBungeecord() && Phantom.getServers().contains(t.getDestination()) && Phantom.getNetworkCount(t.getDestination()) > 0 && Phantom.getNetworkCount(t.getSource()) > 0;
	}
	
	public void fire(Transmission t) throws IOException
	{
		if(t.hasPayload())
		{
			s(responders.size() + "+1");
			responders.add(t);
		}
		
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		boas.write(t.compress());
		new ForwardedPluginMessage(Phantom.instance(), "PhantomTransmission", t.getDestination(), boas).send();
		s(C.GREEN + sname + " -> " + t.getDestination() + C.YELLOW + " [" + t.getType() + "]");
	}
	
	public void transmit(Transmission t) throws IOException
	{
		if(t.getDestination().equals("ALL"))
		{
			for(String i : Phantom.getServers())
			{
				if(i.equals(Phantom.getServerName()))
				{
					continue;
				}
				
				Transmission meta = t.clone();
				meta.set("t.d", i);
				meta.transmit();
			}
			
			return;
		}
		
		if(canFire(t))
		{
			fire(t);
		}
		
		else
		{
			queue.add(t);
			s(C.YELLOW + sname + " |> " + t.getDestination() + C.YELLOW + " [" + t.getType() + "]");
		}
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if(!channel.equals("BungeeCord"))
		{
			return;
		}
		
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		
		if(subchannel.equals("GetServers"))
		{
			GList<String> servers = new GList<String>(in.readUTF().split(", "));
			cc.set("servers", servers);
			
			for(Transmission i : queue.copy())
			{
				if(canFire(i))
				{
					try
					{
						fire(i);
					}
					
					catch(IOException e)
					{
						
					}
					
					queue.remove(i);
				}
			}
		}
		
		else if(subchannel.equals("PlayerCount"))
		{
			String server = in.readUTF();
			int playercount = in.readInt();
			
			cc.set("server." + server + ".count", playercount);
		}
		
		else if(subchannel.equals("PlayerList"))
		{
			String server = in.readUTF();
			String[] playerList = in.readUTF().split(", ");
			
			cc.set("server." + server + ".players", new GList<String>(playerList));
		}
		
		else if(subchannel.equals("GetServer"))
		{
			String server = in.readUTF();
			
			cc.set("this", server);
			sname = server;
			connected = true;
		}
		
		else if(subchannel.equals("PhantomTransmission"))
		{
			short len = in.readShort();
			byte[] msgbytes = new byte[len];
			in.readFully(msgbytes);
			
			try
			{
				Transmission t = new Transmission(msgbytes);
				s(C.AQUA + sname + " <- " + t.getSource() + C.YELLOW + " [" + t.getType() + "]");
				
				for(Transmitter i : transmitters)
				{
					i.onTransmissionReceived(t);
				}
				
				if(t.hasPayload())
				{
					Transmission tt = new Transmission(t.getType() + "-response", t.getSource());
					tt.set("t.k", t.getString("t.r"));
					tt.transmit();
				}
				
				
				if(t.contains("t.k"))
				{
					s(responders.size() + "s");
					
					for(Transmission i : responders.copy())
					{
						if(i.getString("t.r").equals(t.getString("t.k")))
						{
							i.onResponse(t);
							responders.remove(i);
							s("Kff:" + t.getString("t.r"));
						}
					}
				}
			}
			
			catch(IOException e)
			{
				
			}
		}
	}
	
	public DataCluster get()
	{
		return cc;
	}
	
	public boolean connected()
	{
		return connected;
	}
	
	public String getServerName()
	{
		return sname;
	}
}

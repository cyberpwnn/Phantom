package org.phantomapi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONDataInput;
import org.phantomapi.clust.JSONDataOutput;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.BungeeConnectionEstablished;
import org.phantomapi.lang.GList;
import org.phantomapi.network.ForwardedPluginMessage;
import org.phantomapi.network.Network;
import org.phantomapi.network.PhantomNetwork;
import org.phantomapi.network.PluginMessage;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.transmit.Transmission;
import org.phantomapi.transmit.Transmitter;
import org.phantomapi.util.C;
import org.phantomapi.util.Refreshable;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

@Ticked(200)
public class BungeeController extends Controller implements PluginMessageListener, Monitorable
{
	private DataCluster cc;
	private GList<Transmitter> transmitters;
	private GList<Transmission> queue;
	private GList<Transmission> responders;
	private Boolean connected;
	private String sname;
	private Network network;
	private Integer ti;
	private Integer to;
	
	public BungeeController(Controllable parentController)
	{
		super(parentController);
		
		cc = new DataCluster();
		responders = new GList<Transmission>();
		transmitters = new GList<Transmitter>();
		connected = false;
		sname = null;
		queue = new GList<Transmission>();
		network = new PhantomNetwork();
		ti = 0;
		to = 0;
		
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
				hit();
			}
		};
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		if(Phantom.getServerName() == null)
		{
			hit();
		}
	}
	
	public void hit()
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
	
	public boolean canFire(Transmission t)
	{
		return Phantom.instance().isBungeecord() && Phantom.getServers().contains(t.getDestination()) && Phantom.getNetworkCount(t.getDestination()) > 0 && Phantom.getNetworkCount(t.getSource()) > 0;
	}
	
	public void fire(Transmission t) throws IOException
	{
		if(t.hasPayload())
		{
			responders.add(t);
		}
		
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		boas.write(t.compress());
		new ForwardedPluginMessage(Phantom.instance(), "PhantomTransmission", t.getDestination(), boas).send();
		s(C.GREEN + sname + " -> " + t.getDestination() + C.YELLOW + " [" + t.getType() + "]");
		to++;
	}
	
	public void transmit(Transmission t) throws IOException
	{
		if(Phantom.getServers() == null)
		{
			f("Cannot Transmit " + t.getSource() + " :> " + t.getDestination() + " (NO ROUTE)");
			return;
		}
		
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
		File df = new File(Phantom.instance().getDataFolder(), "transmission-queue");
		
		if(df.exists())
		{
			v("Loaded " + df.listFiles().length + " cached transmissions");
			
			for(File i : df.listFiles())
			{
				Transmission t = new Transmission("")
				{
					public void onResponse(Transmission t)
					{
						
					}
				};
				
				try
				{
					new JSONDataInput().load(t, i);
					queue.add(t);
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				i.delete();
			}
		}
		
		df.delete();
	}
	
	@Override
	public void onStop()
	{
		File df = new File(Phantom.instance().getDataFolder(), "transmission-queue");
		
		if(!queue.isEmpty())
		{
			v("Saving " + queue.size() + " pending transmissions...");
			
			df.mkdirs();
			
			for(Transmission i : queue)
			{
				File file = new File(df, i.getSource() + "- -" + i.getDestination() + " [" + i.getTimeStamp() + "]");
				
				if(i.getSource() == null || i.getSource().equals("null"))
				{
					continue;
				}
				
				try
				{
					new JSONDataOutput().save(i, file);
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		queue.clear();
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
			
			if(!connected)
			{
				new TaskLater(20)
				{
					@Override
					public void run()
					{
						s("Bungeecord Connection Established.");
						callEvent(new BungeeConnectionEstablished(Phantom.getServerName(), new GList<String>(Phantom.getServers())));
					}
				};
			}
			
			connected = true;
		}
		
		else if(subchannel.equals("PhantomTransmission"))
		{
			short len = in.readShort();
			byte[] msgbytes = new byte[len];
			in.readFully(msgbytes);
			
			try
			{
				Transmission t = new Transmission(msgbytes)
				{
					public void onResponse(Transmission t)
					{
						
					}
				};
				
				s(C.AQUA + sname + " <- " + t.getSource() + C.YELLOW + " [" + t.getType() + "]");
				ti++;
				
				for(Transmitter i : transmitters)
				{
					i.onTransmissionReceived(t);
				}
				
				if(t.hasPayload() && !t.getType().endsWith("-response"))
				{
					Transmission tt = new Transmission(t.getType() + "-response", t.getSource())
					{
						public void onResponse(Transmission t)
						{
							
						}
					};
					
					tt.set("t.k", t.getString("t.r"));
					tt.transmit();
				}
				
				if(t.contains("t.k"))
				{
					for(Transmission i : responders.copy())
					{
						if(i.getString("t.r").equals(t.getString("t.k")))
						{
							i.onResponse(t);
							responders.remove(i);
						}
					}
				}
			}
			
			catch(IOException e)
			{
				
			}
		}
		
		((Refreshable) network).refresh();
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

	public DataCluster getCc()
	{
		return cc;
	}

	public GList<Transmitter> getTransmitters()
	{
		return transmitters;
	}

	public GList<Transmission> getQueue()
	{
		return queue;
	}

	public GList<Transmission> getResponders()
	{
		return responders;
	}

	public Boolean getConnected()
	{
		return connected;
	}

	public String getSname()
	{
		return sname;
	}

	public Network getNetwork()
	{
		return network;
	}

	public Integer getTi()
	{
		int too = ti;
		ti = 0;
		return too;
	}

	public Integer getTo()
	{
		int too = to;
		to = 0;
		return too;
	}

	@Override
	public String getMonitorableData()
	{
		return "IO: " + C.GREEN + getTi() + C.DARK_GRAY + "/" + C.RED + getTo() + C.DARK_GRAY + " Cache: " + C.LIGHT_PURPLE + queue.size();
	}
}

package org.phantomapi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.network.PluginMessage;
import org.phantomapi.sync.TaskLater;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

@Ticked(200)
public class BungeeController extends Controller implements PluginMessageListener
{
	private DataCluster cc;
	
	public BungeeController(Controllable parentController)
	{
		super(parentController);
		
		cc = new DataCluster();
		
		getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(getPlugin(), "BungeeCord");
		getPlugin().getServer().getMessenger().registerIncomingPluginChannel(getPlugin(), "BungeeCord", this);	
	}
	
	public void onTick()
	{
		new TaskLater((int)(Math.random() * 10))
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
		}
	}
	
	public DataCluster get()
	{
		return cc;
	}
}

package org.phantomapi.network;

import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.util.Refreshable;

/**
 * Implementation of a refreshable phantom server
 * 
 * @author cyberpwn
 */
public abstract class PhantomServer implements NetworkedServer, Refreshable
{
	private String name;
	private GList<String> players;
	private Network network;
	private ServerProperties properties;
	
	public PhantomServer(Network network, String name)
	{
		this.network = network;
		this.name = name;
		properties = new ServerProperties();
		players = new GList<String>();
	}
	
	@Override
	public abstract void refresh();
	
	@Override
	public Network getNetwork()
	{
		return network;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public GList<String> getPlayers()
	{
		if(!isRemote())
		{
			players.clear();
			
			for(Player i : Phantom.instance().onlinePlayers())
			{
				players.add(i.getName());
			}
		}
		
		return players.copy();
	}
	
	@Override
	public Boolean isRemote()
	{
		return !name.equals(Phantom.getServerName());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o != null && o instanceof PhantomServer)
		{
			return ((PhantomServer) o).getName().equals(getName());
		}
		
		return false;
	}
	
	public void update(GList<String> players)
	{
		if(players != null)
		{
			this.players = players.copy();
		}
	}
	
	@Override
	public void sendPlayer(Player p)
	{
		if(isRemote())
		{
			new PluginMessage(Phantom.instance(), "ConnectOther", p.getName(), getName()).send();
		}
	}
	
	@Override
	public boolean isLobby()
	{
		if(name.toLowerCase().contains("lobby") || name.toLowerCase().contains("hub"))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public ServerProperties getProperties()
	{
		return properties;
	}
}

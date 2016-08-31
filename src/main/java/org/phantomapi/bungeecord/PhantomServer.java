package org.phantomapi.bungeecord;

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
	
	public PhantomServer(Network network, String name)
	{
		this.network = network;
		this.name = name;
		this.players = new GList<String>();
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
		return players.copy();
	}
	
	@Override
	public Boolean isRemote()
	{
		return !name.equals(Phantom.getServerName());
	}
	
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
}

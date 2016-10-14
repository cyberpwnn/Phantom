package org.phantomapi.network;

import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GListAdapter;
import org.phantomapi.util.Refreshable;

/**
 * Implementation of a refreshable phantom network
 * 
 * @author cyberpwn
 */
public class PhantomNetwork implements Network, Refreshable
{
	private GList<NetworkedServer> servers;
	
	public PhantomNetwork()
	{
		this.servers = new GList<NetworkedServer>();
	}
	
	@Override
	public void refresh()
	{
		if(Phantom.getServers() != null && Phantom.getServerName() != null)
		{
			for(String i : Phantom.getServers())
			{
				if(getRemoteServer(i) == null)
				{
					servers.add(new PhantomServer(this, i)
					{
						@Override
						public void refresh()
						{
							update(new GList<String>(Phantom.instance().getBungeeController().get().getStringList("server." + getName() + ".players")));
						}
					});
				}
			}
			
			for(NetworkedServer i : getServers())
			{
				if(Phantom.getServers().contains(i.getName()))
				{
					((Refreshable) i).refresh();
				}
				
				else
				{
					servers.remove(i);
				}
			}
		}
	}
	
	@Override
	public GList<NetworkedServer> getServers()
	{
		return servers.copy();
	}
	
	@Override
	public GList<String> getPlayers()
	{
		GList<String> players = new GList<String>();
		
		for(NetworkedServer i : getServers())
		{
			players.add(i.getPlayers());
		}
		
		return players;
	}
	
	@Override
	public NetworkedServer getLocalServer()
	{
		return getRemoteServer(Phantom.getServerName());
	}
	
	@Override
	public NetworkedServer getRemoteServer(String name)
	{
		for(NetworkedServer i : getServers())
		{
			if(i.getName().equals(name))
			{
				return i;
			}
		}
		
		return null;
	}
	
	@Override
	public GList<NetworkedServer> getRemoteServers()
	{
		return new GList<NetworkedServer>(new GListAdapter<NetworkedServer, NetworkedServer>()
		{
			@Override
			public NetworkedServer onAdapt(NetworkedServer from)
			{
				if(from.isRemote())
				{
					return from;
				}
				
				return null;
			}
		}.adapt(getServers()));
	}
	
	@Override
	public GList<NetworkedServer> getLobbyServers()
	{
		return new GList<NetworkedServer>(new GListAdapter<NetworkedServer, NetworkedServer>()
		{
			@Override
			public NetworkedServer onAdapt(NetworkedServer from)
			{
				if(from.isLobby())
				{
					return from;
				}
				
				return null;
			}
		}.adapt(getServers()));
	}
}

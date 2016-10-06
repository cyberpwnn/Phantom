package org.phantomapi.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.callable.Callable;
import org.phantomapi.callable.Description;
import org.phantomapi.lang.GList;
import org.phantomapi.network.NetworkedServer;

/**
 * Callable utilities
 * 
 * @author cyberpwn
 */
public class U
{
	/**
	 * Shut down the server
	 */
	@Description("Shuts down the server")
	@Callable("stop")
	public static void stop()
	{
		Bukkit.shutdown();
	}
	
	/**
	 * Reloads the server
	 */
	@Description("Reload the server")
	@Callable("reload")
	public static void reload()
	{
		Bukkit.reload();
	}
	
	/**
	 * Thrashes the server's phantom plugins
	 */
	@Description("Thrashes the server's phantom plugins")
	@Callable("thrash")
	public static void thrash()
	{
		Phantom.thrash();
	}
	
	/**
	 * Disperse all players to a lobby server
	 */
	@Description("Disperses all players to a lobby server")
	@Callable("disperse")
	public static void disperse()
	{
		GList<Player> px = Phantom.instance().onlinePlayers().copy();
		
		while(!px.isEmpty())
		{
			for(NetworkedServer i : Phantom.instance().getNetwork().getLobbyServers())
			{
				if(i.isRemote())
				{
					if(px.isEmpty())
					{
						return;
					}
					
					i.sendPlayer(px.pop());
				}
			}
		}
	}
}

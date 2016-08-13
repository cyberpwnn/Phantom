package org.cyberpwn.phantom.construct;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.ConfigurationHandler;
import org.cyberpwn.phantom.lang.GList;

/**
 * A Phantom plugin which supports many things
 * 
 * @author cyberpwn
 *
 */
public abstract class PhantomPlugin extends ControllablePlugin
{
	public abstract void enable();
	
	public abstract void disable();
	
	/**
	 * Call a bukkit event
	 * 
	 * @param event
	 *            the event
	 */
	public void callEvent(Event event)
	{
		getServer().getPluginManager().callEvent(event);
	}
	
	/**
	 * Message a commandsender
	 * 
	 * @param sender
	 *            the sender
	 * @param msg
	 *            the message
	 */
	public void msg(CommandSender sender, String msg)
	{
		sender.sendMessage(msg);
	}
	
	/**
	 * Message a commandsender
	 * 
	 * @param sender
	 *            the sender
	 * @param msgs
	 *            the message list
	 */
	public void msg(CommandSender sender, String[] msgs)
	{
		for(String i : msgs)
		{
			msg(sender, i);
		}
	}
	
	/**
	 * Load a cluster
	 * @param c the data cluster
	 */
	public void loadCluster(Configurable c)
	{
		loadCluster(c, null);
	}
	
	/**
	 * Load a cluster
	 * @param c the data cluster
	 */
	public void loadCluster(Configurable c, String category)
	{
		File base = getDataFolder();
		
		if(category != null)
		{
			base = new File(base, category);
		}
		
		try
		{
			ConfigurationHandler.read(base, c);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Online players
	 * @return the player list
	 */
	public GList<Player> onlinePlayers()
	{
		GList<Player> p = new GList<Player>();
		p.addAll(Bukkit.getOnlinePlayers());
		return p;
	}
	
	/**
	 * Could you get a player by searching the keyword
	 * @param search the keyword
	 * @return true if that player matches or somewhat matches
	 */
	public boolean canFindPlayer(String search)
	{
		return findPlayer(search) == null ? false : true;
	}
	
	/**
	 * Find a player. This can match cyb in cyberpwn
	 * @param search the search key
	 * @return a player object or null if none can be found.
	 */
	public Player findPlayer(String search)
	{
		for(Player i : onlinePlayers())
		{
			if(i.getName().equalsIgnoreCase(search))
			{
				return i;
			}
		}
		
		for(Player i : onlinePlayers())
		{
			if(i.getName().toLowerCase().contains(search.toLowerCase()))
			{
				return i;
			}
		}
		
		return null;
	}
}

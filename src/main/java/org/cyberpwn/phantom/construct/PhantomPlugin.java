package org.cyberpwn.phantom.construct;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.ConfigurationHandler;

public class PhantomPlugin extends ControllablePlugin
{
	public void callEvent(Event event)
	{
		getServer().getPluginManager().callEvent(event);
	}
	
	public void msg(CommandSender sender, String msg)
	{
		sender.sendMessage(msg);
	}
	
	public void msg(CommandSender sender, String[] msgs)
	{
		for(String i : msgs)
		{
			msg(sender, i);
		}
	}
	
	public void loadCluster(Configurable c)
	{
		loadCluster(c, null);
	}
	
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
	
	public Collection<? extends Player> onlinePlayers()
	{
		return getServer().getOnlinePlayers();
	}
	
	public boolean canFindPlayer(String search)
	{
		return findPlayer(search) == null ? false : true;
	}
	
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

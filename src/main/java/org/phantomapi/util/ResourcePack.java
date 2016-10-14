package org.phantomapi.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.phantomapi.Phantom;
import org.phantomapi.event.ResourcePackAcceptedEvent;
import org.phantomapi.event.ResourcePackDeclinedEvent;
import org.phantomapi.event.ResourcePackFailedEvent;
import org.phantomapi.event.ResourcePackLoadedEvent;
import org.phantomapi.lang.GList;
import org.phantomapi.sync.Task;

/**
 * Represents a resource pack
 * 
 * @author cyberpwn
 */
public abstract class ResourcePack implements Listener
{
	private String url;
	private GList<Player> pending;
	private Integer refuseDelay;
	
	/**
	 * Create a resource pack util
	 * 
	 * @param url
	 *            the url
	 */
	public ResourcePack(String url)
	{
		this.url = url;
		this.pending = new GList<Player>();
		this.refuseDelay = 600;
	}
	
	/**
	 * Send a resource pack to the given player
	 * 
	 * @param p
	 *            the player
	 */
	public void send(Player p)
	{
		if(pending.contains(p))
		{
			return;
		}
		
		pending.add(p);
		Phantom.instance().getResourceController().send(p, url);
		FinalInteger i = new FinalInteger(refuseDelay);
		
		new Task(0)
		{
			@Override
			public void run()
			{
				i.sub(1);
				
				if(!pending.contains(p))
				{
					cancel();
				}
				
				if(i.get() < 0)
				{
					cancel();
					Phantom.instance().callEvent(new ResourcePackDeclinedEvent(p));
				}
			}
		};
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		pending.remove(e.getPlayer());
	}
	
	@EventHandler
	public void on(ResourcePackDeclinedEvent e)
	{
		pending.remove(e.getPlayer());
		onDecline(e.getPlayer());
	}
	
	@EventHandler
	public void on(ResourcePackFailedEvent e)
	{
		pending.remove(e.getPlayer());
		onFail(e.getPlayer());
	}
	
	@EventHandler
	public void on(ResourcePackAcceptedEvent e)
	{
		pending.remove(e.getPlayer());
		onAccept(e.getPlayer());
	}
	
	@EventHandler
	public void on(ResourcePackLoadedEvent e)
	{
		pending.remove(e.getPlayer());
		onLoaded(e.getPlayer());
	}
	
	public abstract void onAccept(Player p);
	
	public abstract void onLoaded(Player p);
	
	public abstract void onDecline(Player p);
	
	public abstract void onFail(Player p);
}

package org.cyberpwn.phantom.event.structure;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Represents a phantom evnet
 * @author cyberpwn
 *
 */
public class PhantomEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	
	/**
	 * Handler crap
	 */
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	/**
	 * Handler crap
	 */
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}

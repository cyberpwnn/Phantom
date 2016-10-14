package org.phantomapi.game;

/**
 * Represents a base game event
 * 
 * @author cyberpwn
 */
public class BaseGameEvent
{
	private boolean cancelled;
	
	/**
	 * Create a base event
	 */
	public BaseGameEvent()
	{
		this.cancelled = false;
	}
	
	/**
	 * Set this event as cancelled or not
	 * 
	 * @param cancelled
	 *            true if it is
	 */
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
	
	/**
	 * Has this event been cancelled?
	 * 
	 * @return true if it has
	 */
	public boolean isCancelled()
	{
		return cancelled;
	}
}

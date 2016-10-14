package org.phantomapi.event;

import org.bukkit.event.Cancellable;

/**
 * Represents a cancellable phantom evnet
 * @author cyberpwn
 *
 */
public class CancellablePhantomEvent extends PhantomEvent implements Cancellable
{
	private boolean cancelled;
	
	public CancellablePhantomEvent()
	{
		cancelled = false;
	}

	/**
	 * Checks if the event has been cancelled
	 */
	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	/**
	 * Set the event to be cancelled .. or not?
	 */
	@Override
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
	
	/**
	 * Same thing as setCancelled(true)
	 */
	public void cancel()
	{
		this.cancelled = true;
	}
	
	/**
	 * Same thing as setCancelled(false)
	 */
	public void uncancel()
	{
		this.cancelled = false;
	}
}

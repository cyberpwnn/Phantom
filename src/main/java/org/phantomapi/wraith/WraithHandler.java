package org.phantomapi.wraith;

import org.bukkit.event.Listener;

/**
 * Represents a wraith handler
 * 
 * @author cyberpwn
 */
public interface WraithHandler extends Listener
{
	/**
	 * Get the wraith
	 * 
	 * @return the wraith
	 */
	public Wraith getWraith();
	
	/**
	 * Get the name of this handler
	 * 
	 * @return the handler name
	 */
	public String getName();
	
	/**
	 * Unregister this wraith handler
	 */
	public void unregister();
	
	/**
	 * Handler ticked
	 */
	public void onTick();
	
	/**
	 * Handler bound
	 */
	public void onUnbind();
	
	/**
	 * Handler unbound
	 */
	public void onBind();
}

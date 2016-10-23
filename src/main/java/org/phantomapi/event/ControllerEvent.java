package org.phantomapi.event;

import org.bukkit.plugin.Plugin;
import org.phantomapi.construct.Controllable;

/**
 * Represents a controller event
 * 
 * @author cyberpwn
 */
public class ControllerEvent extends PhantomEvent
{
	private final Controllable controllable;
	private final Plugin plugin;
	
	public ControllerEvent(Controllable controllable, Plugin plugin)
	{
		this.controllable = controllable;
		this.plugin = plugin;
	}
	
	/**
	 * Get the controller
	 * 
	 * @return the controller
	 */
	public Controllable getControllable()
	{
		return controllable;
	}
	
	/**
	 * The controller host (the plugin)
	 * 
	 * @return
	 */
	public Plugin getPlugin()
	{
		return plugin;
	}
}

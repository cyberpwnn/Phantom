package org.phantomapi.event;

import org.bukkit.plugin.Plugin;
import org.phantomapi.construct.Controllable;

/**
 * Called when a controller is stopped
 * 
 * @author cyberpwn
 */
public class ControllerStopEvent extends ControllerEvent
{
	public ControllerStopEvent(Controllable controllable, Plugin plugin)
	{
		super(controllable, plugin);
	}
}

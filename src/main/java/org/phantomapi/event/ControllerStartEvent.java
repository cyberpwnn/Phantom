package org.phantomapi.event;

import org.bukkit.plugin.Plugin;
import org.phantomapi.construct.Controllable;

/**
 * Called when a controller is started
 * 
 * @author cyberpwn
 */
public class ControllerStartEvent extends ControllerEvent
{
	public ControllerStartEvent(Controllable controllable, Plugin plugin)
	{
		super(controllable, plugin);
	}
}

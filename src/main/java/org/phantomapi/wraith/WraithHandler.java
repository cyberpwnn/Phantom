package org.phantomapi.wraith;

import org.bukkit.event.Listener;

public interface WraithHandler extends Listener
{
	public Wraith getWraith();
	
	public String getName();
}

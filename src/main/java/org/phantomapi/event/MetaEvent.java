package org.phantomapi.event;

import org.bukkit.World;

public class MetaEvent extends PhantomEvent
{
	private World world;
	
	public MetaEvent(World world)
	{
		this.world = world;
	}
	
	public World getWorld()
	{
		return world;
	}
}

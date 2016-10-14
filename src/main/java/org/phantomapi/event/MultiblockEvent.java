package org.phantomapi.event;

import org.bukkit.World;
import org.phantomapi.multiblock.Multiblock;

/**
 * Represents a multiblock event
 * 
 * @author cyberpwn
 */
public class MultiblockEvent extends PhantomEvent
{
	private final Multiblock multiblock;
	private final World world;
	
	public MultiblockEvent(Multiblock multiblock, World world)
	{
		this.multiblock = multiblock;
		this.world = world;
	}
	
	/**
	 * Get the multiblock instance
	 * 
	 * @return the instance
	 */
	public Multiblock getMultiblock()
	{
		return multiblock;
	}
	
	/**
	 * Get the world
	 * 
	 * @return the world
	 */
	public World getWorld()
	{
		return world;
	}
}

package org.phantomapi.event;

import org.bukkit.World;
import org.phantomapi.multiblock.Multiblock;

/**
 * A Multiblock instance loaded
 * 
 * @author cyberpwn
 */
public class MultiblockLoadEvent extends MultiblockEvent
{
	public MultiblockLoadEvent(Multiblock multiblock, World world)
	{
		super(multiblock, world);
	}
}

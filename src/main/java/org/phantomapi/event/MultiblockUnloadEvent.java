package org.phantomapi.event;

import org.bukkit.World;
import org.phantomapi.multiblock.Multiblock;

/**
 * A Multiblock instance unloaded
 * 
 * @author cyberpwn
 */
public class MultiblockUnloadEvent extends MultiblockEvent
{
	public MultiblockUnloadEvent(Multiblock multiblock, World world)
	{
		super(multiblock, world);
	}
}

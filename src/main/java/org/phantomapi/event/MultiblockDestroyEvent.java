package org.phantomapi.event;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.phantomapi.multiblock.Multiblock;

/**
 * A Multiblock structre was destroyed
 * 
 * @author cyberpwn
 */
public class MultiblockDestroyEvent extends MultiblockPlayerEvent
{
	public MultiblockDestroyEvent(Player player, Multiblock multiblock, World world, Block block)
	{
		super(player, multiblock, world, block);
	}
}

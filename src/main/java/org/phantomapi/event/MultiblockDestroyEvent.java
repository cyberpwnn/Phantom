package org.phantomapi.event;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.phantomapi.multiblock.Multiblock;

/**
 * A Multiblock structre was destroyed
 * 
 * @author cyberpwn
 */
public class MultiblockDestroyEvent extends MultiblockPlayerEvent
{
	public MultiblockDestroyEvent(Player player, Multiblock multiblock, World world)
	{
		super(player, multiblock, world);
	}
}

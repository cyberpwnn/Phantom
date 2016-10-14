package org.phantomapi.event;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.phantomapi.multiblock.Multiblock;

/**
 * A Multiblock structre was created
 * 
 * @author cyberpwn
 */
public class MultiblockConstructEvent extends MultiblockPlayerEvent
{
	public MultiblockConstructEvent(Player player, Multiblock multiblock, World world)
	{
		super(player, multiblock, world);
	}
}

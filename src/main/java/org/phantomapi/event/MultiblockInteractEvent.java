package org.phantomapi.event;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.phantomapi.multiblock.Multiblock;

/**
 * A Multiblock structre was created
 * 
 * @author cyberpwn
 */
public class MultiblockInteractEvent extends MultiblockPlayerEvent
{
	private final Action action;
	
	public MultiblockInteractEvent(Player player, Multiblock multiblock, World world, Action action)
	{
		super(player, multiblock, world);
		
		this.action = action;
	}
	
	/**
	 * Get the action type (left or right block) air is never used
	 * 
	 * @return the action
	 */
	public Action getAction()
	{
		return action;
	}
}

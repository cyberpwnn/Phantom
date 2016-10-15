package org.phantomapi.event;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.phantomapi.multiblock.Multiblock;

/**
 * Represents a player related multiblock event
 * 
 * @author cyberpwn
 */
public class MultiblockPlayerEvent extends MultiblockCancellableEvent
{
	private final Block block;
	private final Player player;
	
	public MultiblockPlayerEvent(Player player, Multiblock multiblock, World world, Block block)
	{
		super(multiblock, world);
		
		this.player = player;
		this.block = block;
	}
	
	/**
	 * Get the player
	 * 
	 * @return the player
	 */
	public Player getPlayer()
	{
		return player;
	}
	
	/**
	 * Get the block related to this event
	 * 
	 * @return the block
	 */
	public Block getBlock()
	{
		return block;
	}
}

package org.phantomapi.event;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.phantomapi.multiblock.Multiblock;

/**
 * Represents a player related multiblock event
 * 
 * @author cyberpwn
 */
public class MultiblockPlayerEvent extends MultiblockCancellableEvent
{
	private final Player player;
	
	public MultiblockPlayerEvent(Player player, Multiblock multiblock, World world)
	{
		super(multiblock, world);
		
		this.player = player;
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
}

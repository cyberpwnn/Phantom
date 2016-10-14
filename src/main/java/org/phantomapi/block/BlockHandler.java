package org.phantomapi.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Block handler
 * 
 * @author cyberpwn
 */
public interface BlockHandler
{
	/**
	 * Can the given block be modified by a player
	 * 
	 * @param p
	 *            the player
	 * @param block
	 *            the block
	 * @return true if it can be modified
	 */
	public boolean canModify(Player p, Block block);
	
	/**
	 * Does the block have any protection?
	 * 
	 * @param block
	 *            the block
	 * @return true if it does
	 */
	public boolean hasProtection(Block block);
	
	/**
	 * Get the protector
	 * 
	 * @return the protector
	 */
	public String getProtector();
	
	/**
	 * Get the protector for this block
	 * 
	 * @param block
	 *            the specific block
	 * @return the protector for the given block
	 */
	public String getProtector(Block block);
}

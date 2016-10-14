package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.block.BlockHandler;
import org.phantomapi.lang.GList;

/**
 * Blocks utilities
 * 
 * @author cyberpwn
 */
public class Blocks
{
	/**
	 * Get the center of the block
	 * 
	 * @param block
	 *            the block
	 * @return the center location
	 */
	public static Location getCenter(Block block)
	{
		return block.getLocation().clone().add(0.5, 0.5, 0.5);
	}
	
	/**
	 * Can the player modify the given block based on region handlers
	 * 
	 * @param p
	 *            the player
	 * @param block
	 *            the block
	 * @return true if the player can mine or place a block at the given
	 *         location
	 */
	public static boolean canModify(Player p, Block block)
	{
		return getModify(p, block).isEmpty();
	}
	
	/**
	 * Get all handlers
	 * 
	 * @return the handlers
	 */
	public static GList<BlockHandler> getHandlers()
	{
		return Phantom.instance().getBlockCheckController().getHandlers();
	}
	
	/**
	 * Get all preventing modifications due to the following handlers
	 * 
	 * @param p
	 *            the player
	 * @param block
	 *            the block
	 * @return the handlers that have cancelled the modification
	 */
	public static GList<BlockHandler> getModify(Player p, Block block)
	{
		return Phantom.instance().getBlockCheckController().canModify(p, block);
	}
}

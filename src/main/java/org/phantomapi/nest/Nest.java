package org.phantomapi.nest;

import org.bukkit.block.Block;
import org.phantomapi.Phantom;

/**
 * Nest utilities
 * 
 * @author cyberpwn
 */
public class Nest
{
	/**
	 * Get the nested block. Must be sync and a sync block
	 * 
	 * @param block
	 *            the block
	 * @return the nested block or null
	 */
	public NestedBlock get(Block block)
	{
		return Phantom.instance().getNestController().get(block);
	}
}

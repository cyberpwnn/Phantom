package org.phantomapi.nest;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.phantomapi.Phantom;

/**
 * Nest api hub
 * @author cyberpwn
 *
 */
public class Nest
{
	public static NestedChunk getChunk(Chunk c)
	{
		return Phantom.instance().getNestController().get(c);
	}
	
	public static NestedBlock getBlock(Block block)
	{
		return getChunk(block.getChunk()).getBlock(block);
	}
}

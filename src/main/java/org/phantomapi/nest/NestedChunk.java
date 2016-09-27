package org.phantomapi.nest;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

/**
 * Represents a nested chunk
 * 
 * @author cyberpwn
 */
public interface NestedChunk
{
	/**
	 * Get a nested block. If it does not exist, it will be created and cached.
	 * 
	 * @param block
	 *            the block
	 * @return the nested block with any data on it
	 */
	public NestedBlock getBlock(Block block);
	
	/**
	 * Get the chunk for this nest
	 * 
	 * @return the chunk
	 */
	public Chunk getChunk();
}

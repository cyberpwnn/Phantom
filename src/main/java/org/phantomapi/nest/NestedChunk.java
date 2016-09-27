package org.phantomapi.nest;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public interface NestedChunk
{
	public NestedBlock getBlock(Block block);
	
	public Chunk getChunk();
}

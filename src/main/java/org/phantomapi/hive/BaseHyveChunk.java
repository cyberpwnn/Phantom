package org.phantomapi.hive;

import java.io.File;
import org.bukkit.Chunk;

public class BaseHyveChunk extends BaseHyve implements HyveChunk
{
	/**
	 * Create an instance of the hyve chunk. Automatically loads it in. Must use
	 * save() to save any changes
	 * 
	 * @param chunk
	 *            the chunk to load
	 */
	public BaseHyveChunk(Chunk chunk)
	{
		super(HyveType.CHUNK, "hyve-chunk." + chunk.getX() + "." + chunk.getZ() + ".hy", new File(chunk.getWorld().getWorldFolder(), "chunk"));
		
		load();
	}
}

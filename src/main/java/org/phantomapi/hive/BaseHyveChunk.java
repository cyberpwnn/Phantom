package org.phantomapi.hive;

import java.io.File;
import org.bukkit.Chunk;

public class BaseHyveChunk extends BaseHyve implements HyveChunk
{
	public BaseHyveChunk(Chunk chunk)
	{
		super(HyveType.CHUNK, "hyve-chunk." + chunk.getX() + "." + chunk.getZ() + ".hy", new File(chunk.getWorld().getWorldFolder(), "chunk"));
		
		load();
	}
}

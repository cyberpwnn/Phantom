package org.phantomapi.event;

import org.bukkit.Chunk;
import org.phantomapi.blockmeta.ChunkMeta;

public class MetaChunkUnloadEvent extends MetaEvent
{
	private Chunk chunk;
	private ChunkMeta meta;
	
	public MetaChunkUnloadEvent(Chunk chunk, ChunkMeta meta)
	{
		super(chunk.getWorld());
		
		this.chunk = chunk;
	}
	
	public Chunk getChunk()
	{
		return chunk;
	}
	
	public ChunkMeta getMeta()
	{
		return meta;
	}
}

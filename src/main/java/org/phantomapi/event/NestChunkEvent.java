package org.phantomapi.event;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.phantomapi.nest.NestedChunk;

/**
 * Represents a nested chunk based event
 * 
 * @author cyberpwn
 */
public class NestChunkEvent extends PhantomEvent
{
	private final NestedChunk nestedChunk;
	private final Chunk chunk;
	private final World world;
	
	public NestChunkEvent(NestedChunk nestedChunk)
	{
		this.nestedChunk = nestedChunk;
		this.chunk = nestedChunk.getChunk().toChunk();
		this.world = chunk.getWorld();
	}

	public NestedChunk getNestedChunk()
	{
		return nestedChunk;
	}

	public Chunk getChunk()
	{
		return chunk;
	}

	public World getWorld()
	{
		return world;
	}
}

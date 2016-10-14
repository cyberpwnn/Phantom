package org.phantomapi.event;

import org.phantomapi.nest.NestedChunk;

/**
 * The nested chunk is about to unload
 * 
 * @author cyberpwn
 */
public class NestChunkUnloadEvent extends NestChunkEvent
{
	public NestChunkUnloadEvent(NestedChunk nestedChunk)
	{
		super(nestedChunk);
	}
}

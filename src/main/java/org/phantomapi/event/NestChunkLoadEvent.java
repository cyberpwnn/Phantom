package org.phantomapi.event;

import org.phantomapi.nest.NestedChunk;

/**
 * The nested chunk has finished loading
 * 
 * @author cyberpwn
 */
public class NestChunkLoadEvent extends NestChunkEvent
{
	public NestChunkLoadEvent(NestedChunk nestedChunk)
	{
		super(nestedChunk);
	}
}

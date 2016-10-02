package org.phantomapi.nest;

import org.bukkit.block.Block;
import org.phantomapi.lang.GChunk;
import org.phantomapi.lang.GLocation;
import org.phantomapi.lang.GMap;

/**
 * Nested chunk
 * 
 * @author cyberpwn
 */
public class NestedChunk extends NestedObject
{
	private static final long serialVersionUID = 1L;
	private final GMap<GLocation, NestedBlock> blocks;
	private final GChunk chunk;
	
	/**
	 * Create a new nested chunk
	 * 
	 * @param chunk
	 *            the gchunk
	 */
	public NestedChunk(GChunk chunk)
	{
		this.blocks = new GMap<GLocation, NestedBlock>();
		this.chunk = chunk;
	}
	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	public GMap<GLocation, NestedBlock> getBlocks()
	{
		return blocks;
	}
	
	public GChunk getChunk()
	{
		return chunk;
	}
	
	/**
	 * Get the nested block
	 * 
	 * @param block
	 *            the block
	 * @return the nested block
	 */
	public NestedBlock getBlock(Block block)
	{
		GLocation l = new GLocation(block.getLocation());
		
		if(!blocks.containsKey(l))
		{
			blocks.put(l, new NestedBlock(l));
		}
		
		return blocks.get(l);
	}
}

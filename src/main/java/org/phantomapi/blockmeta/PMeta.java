package org.phantomapi.blockmeta;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.phantomapi.Phantom;
import org.phantomapi.core.BlockMetaController;
import org.phantomapi.lang.GChunk;

/**
 * PMeta utilities
 * 
 * @author cyberpwn
 */
public class PMeta
{
	/**
	 * Get the controller
	 * 
	 * @return the controller
	 */
	public static BlockMetaController getController()
	{
		return Phantom.instance().getBlockMetaController();
	}
	
	/**
	 * Get the chunk meta
	 * 
	 * @param chunk
	 *            the chunk
	 * @return the chunk meta or null
	 */
	public static ChunkMeta getChunk(Chunk chunk)
	{
		return getController().getChunk(new GChunk(chunk));
	}
	
	/**
	 * Is the chunk holding chunk meta yet or can?
	 * 
	 * @param chunk
	 *            the chunk
	 * @return true if getChunk will return an actual meta object
	 */
	public static boolean hasChunk(Chunk chunk)
	{
		return getChunk(chunk) != null;
	}
	
	/**
	 * Get the block meta for a block
	 * 
	 * @param block
	 *            the block
	 * @return the block meta
	 */
	public static BlockMeta getBlock(Block block)
	{
		if(hasChunk(block.getChunk()))
		{
			return getChunk(block.getChunk()).getBlock(block);
		}
		
		return null;
	}
	
	/**
	 * Is the given block ready for meta?
	 * 
	 * @param block
	 *            the block
	 * @return true if getBlock would return an actual meta object
	 */
	public static boolean hasBlock(Block block)
	{
		return hasChunk(block.getChunk());
	}
}

package org.phantomapi.world;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.phantomapi.Phantom;

/**
 * Relighting suggestions and queues
 * 
 * @author cyberpwn
 */
public class Photon
{
	/**
	 * Relight the given chunk
	 * 
	 * @param chunk
	 *            the chunk to relight
	 */
	public static void relight(Chunk chunk)
	{
		for(Chunk i : W.chunkRadius(chunk, 2))
		{
			Phantom.instance().getPhotonController().relight(i);
		}
	}
	
	/**
	 * Relight the given location
	 * 
	 * @param location
	 *            the location
	 */
	public static void relight(Location location)
	{
		relight(location.getChunk());
	}
	
	/**
	 * Relight the given block
	 * 
	 * @param block
	 *            the block to relight
	 */
	public static void relight(Block block)
	{
		relight(block.getChunk());
	}
}

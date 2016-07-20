package org.cyberpwn.phantom.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.cyberpwn.phantom.lang.GList;

/**
 * World utils
 * 
 * @author cyberpwn
 *
 */
public class W
{
	/**
	 * Chunk faces around a given chunk
	 * 
	 * @param c
	 *            the chunk
	 * @return the surrounding 4 chunks
	 */
	public static GList<Chunk> chunkFaces(Chunk c)
	{
		GList<Chunk> cx = new GList<Chunk>();
		
		cx.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ()));
		cx.add(c.getWorld().getChunkAt(c.getX() - 1, c.getZ()));
		cx.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 1));
		cx.add(c.getWorld().getChunkAt(c.getX(), c.getZ() - 1));
		
		return cx;
	}
	
	/**
	 * Get all 6 blocks touching a given block
	 * 
	 * @param b
	 *            the block
	 * @return the surrounding 6 blocks
	 */
	public static GList<Block> blockFaces(Block b)
	{
		GList<Block> blocks = new GList<Block>();
		
		blocks.add(b.getRelative(BlockFace.UP));
		blocks.add(b.getRelative(BlockFace.DOWN));
		blocks.add(b.getRelative(BlockFace.NORTH));
		blocks.add(b.getRelative(BlockFace.SOUTH));
		blocks.add(b.getRelative(BlockFace.EAST));
		blocks.add(b.getRelative(BlockFace.WEST));
		
		return blocks;
	}
	
	/**
	 * simulate a fall from a location
	 * 
	 * @param from
	 *            the location to fall from
	 * @return the location where it would fall to
	 */
	public static Location simulateFall(Location from)
	{
		int height = from.getBlockY();
		
		for(int i = height; i > 0; i--)
		{
			int check = i - 1;
			
			Material type = new Location(from.getWorld(), from.getBlockX(), check, from.getBlockZ()).getBlock().getType();
			
			if(!(type.equals(Material.AIR) || type.equals(Material.WATER) || type.equals(Material.STATIONARY_WATER) || type.equals(Material.LAVA) || type.equals(Material.STATIONARY_LAVA)))
			{
				return new Location(from.getWorld(), from.getBlockX(), check + 1, from.getBlockZ());
			}
		}
		
		return null;
	}
	
	/**
	 * Get a radius area of chunks around a given chunk
	 * 
	 * @param c
	 *            the chunk center
	 * @param rad
	 *            the radius
	 * @return the chunks including the center given chunk
	 */
	public static GList<Chunk> chunkRadius(Chunk c, int rad)
	{
		GList<Chunk> cx = new GList<Chunk>();
		
		for(int i = c.getX() - rad; i < c.getX() + rad; i++)
		{
			for(int j = c.getZ() - rad; j < c.getZ() + rad; j++)
			{
				cx.add(c.getWorld().getChunkAt(i, j));
			}
		}
		
		cx.add(c);
		
		return cx;
	}
}

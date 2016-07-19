package org.cyberpwn.phantom.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.cyberpwn.phantom.lang.GList;

public class W
{
	public static GList<Chunk> chunkFaces(Chunk c)
	{
		GList<Chunk> cx = new GList<Chunk>();
		
		cx.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ()));
		cx.add(c.getWorld().getChunkAt(c.getX() - 1, c.getZ()));
		cx.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 1));
		cx.add(c.getWorld().getChunkAt(c.getX(), c.getZ() - 1));
		
		return cx;
	}
	
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

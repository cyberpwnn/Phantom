package org.cyberpwn.phantom.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;
import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.world.Area;
import org.cyberpwn.phantom.world.RayTrace;

/**
 * World utils
 * 
 * @author cyberpwn
 *
 */
public class W
{
	/**
	 * Checks if a given player can modify a given block
	 * 
	 * @param p
	 *            the player
	 * @param block
	 *            the block
	 * @return true if the player can break it
	 */
	public static boolean canModify(Player p, Block block)
	{
		BlockBreakEvent bbe = new BlockBreakEvent(block, p);
		Phantom.instance().callEvent(bbe);
		return !bbe.isCancelled();
	}
	
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
	
	/**
	 * Get an entity that the supplied entity (e) is looking at with a specific
	 * range and offset
	 * 
	 * @param e
	 *            the entity
	 * @param range
	 *            the max range to check for. If this is less than 1, 1 will be
	 *            used instead.
	 * @param off
	 *            the offeset. For example, if this is set to 2, then you cannot
	 *            be looking at an entity if it is at least 3 or more blocks
	 *            away from your target. If the offset is less than 1, 1 will be
	 *            used instead
	 * @return an entity that the supplied entity (e) is looking at. If the
	 *         supplied entity is not looking at an entity, or it does not meet
	 *         the given ranges and offsets, null will be returned instead
	 */
	public static Entity getEntityLookingAt(Entity e, double range, double off)
	{
		if(off < 1)
		{
			off = 1;
		}
		
		if(range < 1)
		{
			range = 1;
		}
		
		final Double doff = off;
		final Entity[] result = new Entity[1];
		
		new RayTrace(e.getLocation(), e.getLocation().getDirection(), range, (double) 1)
		{
			public void onTrace(Location l)
			{
				Area a = new Area(l, doff);
				
				for(Entity i : a.getNearbyEntities())
				{
					if(!e.equals(i))
					{
						stop();
						result[0] = i;
						return;
					}
				}
			}
		}.trace();
		
		return result[0];
	}
	
	/**
	 * Check if the given entity IS is looking at the given entity AT.
	 * 
	 * @param is
	 *            the entity
	 * @param at
	 *            the entity that IS should be looking at to return true
	 * @param range
	 *            the max range to check
	 * @param off
	 *            the max offset
	 * @return true if the entity IS is looking at the given entity AT
	 */
	public static boolean isLookingAt(Entity is, Entity at, double range, double off)
	{
		if(off < 1)
		{
			off = 1;
		}
		
		if(range < 1)
		{
			range = 1;
		}
		
		final Double doff = off;
		final Entity[] result = new Entity[1];
		
		new RayTrace(is.getLocation(), is.getLocation().getDirection(), range, (double) 1)
		{
			public void onTrace(Location l)
			{
				Area a = new Area(l, doff);
				
				for(Entity i : a.getNearbyEntities())
				{
					if(!is.equals(i) && i.equals(at))
					{
						stop();
						result[0] = i;
						return;
					}
				}
			}
		}.trace();
		
		return result[0] != null && result[0].equals(at);
	}
	
	/**
	 * Get the difference between two vectors (squared)
	 * 
	 * @param a
	 *            the first vector
	 * @param b
	 *            the second vector
	 * @return the difference
	 */
	public static double differenceOfVectors(Vector a, Vector b)
	{
		return a.distanceSquared(b);
	}
}

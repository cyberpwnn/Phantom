package org.phantomapi.util;

import java.util.Collection;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.world.Area;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.RayTrace;

/**
 * World utils
 * 
 * @author cyberpwn
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
	 * Get the amount of the given item a player has
	 * 
	 * @param p
	 *            the player
	 * @param mb
	 *            the materialblock
	 * @return the amount they have
	 */
	@SuppressWarnings("deprecation")
	public static int count(Player p, MaterialBlock mb)
	{
		int has = 0;
		
		for(ItemStack i : p.getInventory().getContents())
		{
			if(i != null)
			{
				if(i.getType().equals(mb.getMaterial()) && (i.getData().getData() == -1 ? 0 : i.getData().getData()) == mb.getData())
				{
					has += i.getAmount();
				}
			}
		}
		
		return has;
	}
	
	/**
	 * Check if the player has a certain amount of items in their inventory (or
	 * more)
	 * 
	 * @param p
	 *            the player
	 * @param mb
	 *            the material block
	 * @param amt
	 *            the amount they need at least
	 * @return true if they have enough
	 */
	public static boolean has(Player p, MaterialBlock mb, int amt)
	{
		return count(p, mb) >= amt;
	}
	
	/**
	 * Take items from the player's inventory
	 * 
	 * @param p
	 *            the player
	 * @param mb
	 *            the type
	 * @param amt
	 *            the amount
	 */
	@SuppressWarnings("deprecation")
	public static void take(Player p, MaterialBlock mb, int amt)
	{
		if(has(p, mb, amt))
		{
			for(int i = 0; i < amt; i++)
			{
				p.getInventory().removeItem(new ItemStack(mb.getMaterial(), 1, (short) 0, mb.getData()));
			}
		}
	}
	
	/**
	 * Calculates the so-called 'Manhatten Distance' between two locations<br>
	 * This is the distance between two points without going diagonally
	 * 
	 * @param b1
	 *            location
	 * @param b2
	 *            location
	 * @param checkY
	 *            state, True to include the y distance, False to exclude it
	 * @return The manhattan distance
	 */
	public static int getManhattanDistance(Location b1, Location b2, boolean checkY)
	{
		int d = Math.abs(b1.getBlockX() - b2.getBlockX());
		d += Math.abs(b1.getBlockZ() - b2.getBlockZ());
		
		if(checkY)
		{
			d += Math.abs(b1.getBlockY() - b2.getBlockY());
		}
		
		return d;
	}
	
	/**
	 * Calculates the so-called 'Manhatten Distance' between two blocks<br>
	 * This is the distance between two points without going diagonally
	 * 
	 * @param b1
	 *            block
	 * @param b2
	 *            block
	 * @param checkY
	 *            state, True to include the y distance, False to exclude it
	 * @return The Manhattan distance
	 */
	public static int getManhattanDistance(Block b1, Block b2, boolean checkY)
	{
		int d = Math.abs(b1.getX() - b2.getX());
		d += Math.abs(b1.getZ() - b2.getZ());
		
		if(checkY)
		{
			d += Math.abs(b1.getY() - b2.getY());
		}
		
		return d;
	}
	
	/**
	 * Tries to get a material from a string including meta (excluding it
	 * implies 0)
	 * </br>
	 * </br>
	 * 1, STONE, stone, 1:0 all would return STONE (byte = 0)
	 * 
	 * @param s
	 *            the input string
	 * @return the MaterialBlock, NULL if it cannot parse.
	 */
	@SuppressWarnings("deprecation")
	public static MaterialBlock getMaterialBlock(String s)
	{
		Material material = null;
		Byte meta = (byte) 0;
		String m = "0";
		String b = "0";
		
		if(s.contains(":"))
		{
			m = s.split(":")[0];
			b = s.split(":")[1];
		}
		
		else
		{
			m = s;
		}
		
		try
		{
			material = Material.getMaterial(Integer.valueOf(m));
			
			if(material == null)
			{
				try
				{
					material = Material.valueOf(m.toUpperCase());
					
					if(material == null)
					{
						return null;
					}
				}
				
				catch(Exception e)
				{
					return null;
				}
			}
		}
		
		catch(Exception e)
		{
			try
			{
				material = Material.valueOf(m.toUpperCase());
				
				if(material == null)
				{
					return null;
				}
			}
			
			catch(Exception ex)
			{
				return null;
			}
		}
		
		try
		{
			meta = Integer.valueOf(b).byteValue();
		}
		
		catch(Exception e)
		{
			meta = (byte) 0;
		}
		
		return new MaterialBlock(material, meta);
	}
	
	/**
	 * Gets all the Blocks relative to a main block using multiple Block Faces
	 * 
	 * @param main
	 *            block
	 * @param faces
	 *            to get the blocks relative to the main of
	 * @return An array of relative blocks to the main based on the input faces
	 */
	public static Block[] getRelative(Block main, BlockFace... faces)
	{
		if(main == null)
		{
			return new Block[0];
		}
		
		Block[] rval = new Block[faces.length];
		
		for(int i = 0; i < rval.length; i++)
		{
			rval[i] = main.getRelative(faces[i]);
		}
		
		return rval;
	}
	
	/**
	 * Sets the Block type and data at once, then performs physics
	 * 
	 * @param block
	 *            to set the type and data of
	 * @param type
	 *            to set to
	 * @param data
	 *            to set to
	 */
	public static void setTypeAndData(Block block, Material type, MaterialData data)
	{
		setTypeAndData(block, type, data, true);
	}
	
	/**
	 * Sets the Block type and data at once
	 * 
	 * @param block
	 *            to set the type and data of
	 * @param type
	 *            to set to
	 * @param data
	 *            to set to
	 * @param update
	 *            - whether to perform physics afterwards
	 */
	@SuppressWarnings("deprecation")
	public static void setTypeAndData(Block block, Material type, MaterialData data, boolean update)
	{
		block.setTypeIdAndData(type.getId(), data.getData(), update);
	}
	
	/**
	 * Sets the Block type and data at once, then performs physics
	 * 
	 * @param block
	 *            to set the type and data of
	 * @param type
	 *            to set to
	 * @param data
	 *            to set to
	 */
	public static void setTypeAndRawData(Block block, Material type, int data)
	{
		setTypeAndRawData(block, type, data, true);
	}
	
	/**
	 * Sets the Block type and data at once
	 * 
	 * @param block
	 *            to set the type and data of
	 * @param type
	 *            to set to
	 * @param data
	 *            to set to
	 * @param update
	 *            - whether to perform physics afterwards
	 */
	@SuppressWarnings("deprecation")
	public static void setTypeAndRawData(Block block, Material type, int data, boolean update)
	{
		block.setTypeIdAndData(type.getId(), (byte) data, update);
	}
	
	/**
	 * Sets the Material Data for a Block
	 * 
	 * @param block
	 *            to set it for
	 * @param materialData
	 *            to set to
	 */
	@SuppressWarnings("deprecation")
	public static void setData(Block block, MaterialData materialData)
	{
		block.setData(materialData.getData());
	}
	
	/**
	 * Sets the Material Data for a Block
	 * 
	 * @param block
	 *            to set it for
	 * @param materialData
	 *            to set to
	 * @param doPhysics
	 *            - True to perform physics, False for 'silent'
	 */
	@SuppressWarnings("deprecation")
	public static void setData(Block block, MaterialData materialData, boolean doPhysics)
	{
		block.setData(materialData.getData(), doPhysics);
	}
	
	/**
	 * Gets the highest level of a type of potion from a list of potion effect
	 * objects
	 * 
	 * @param type
	 *            the potion effect type
	 * @param pots
	 *            the potions collection
	 * @return the level. This is one added to the amplifier. So if there are NO
	 *         potion effects of that type, 0 is returned. However if the
	 *         highest amplifier is 0, then 1 will be returned and so on.
	 */
	public static int getHighestPotionLevel(PotionEffectType type, Collection<PotionEffect> pots)
	{
		int highest = 0;
		
		for(PotionEffect i : pots)
		{
			if(i.getType().equals(type) && i.getAmplifier() + 1 > highest)
			{
				highest = i.getAmplifier() + 1;
			}
		}
		
		return highest;
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
		Entity e = getEntityLookingAt(is, range, off);
		
		if(e == null)
		{
			return false;
		}
		
		return e.equals(at);
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

package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.phantomapi.lang.GMap;

/**
 * A world queue for all worlds. Dont forget to flush
 * 
 * @author cyberpwn
 */
public class PhantomWorldQueue
{
	private GMap<String, PhantomEditSession> sessions;
	
	/**
	 * Create a world queue
	 */
	public PhantomWorldQueue()
	{
		sessions = new GMap<String, PhantomEditSession>();
	}
	
	/**
	 * Get an edit session for the given world. If it does not exist, it will be
	 * created
	 * 
	 * @param world
	 *            the world
	 * @return the new or existing edit session
	 */
	public PhantomEditSession getSession(World world)
	{
		if(!sessions.containsKey(world.getName()))
		{
			sessions.put(world.getName(), new PhantomEditSession(new PhantomWorld(world)));
		}
		
		return sessions.get(world.getName());
	}
	
	/**
	 * Set a given block to the materialblock
	 * 
	 * @param location
	 *            the location
	 * @param block
	 *            the materialblock
	 */
	public void set(Location location, MaterialBlock block)
	{
		getSession(location.getWorld()).set(location, block);
	}
	
	/**
	 * Set a given block to the materialblock
	 * 
	 * @param location
	 *            the location
	 * @param material
	 *            the material
	 * @param data
	 *            the byte data
	 */
	public void set(Location location, Material material, Byte data)
	{
		getSession(location.getWorld()).set(location, material, data);
	}
	
	/**
	 * Set a given block to the materialblock
	 * 
	 * @param location
	 *            the location
	 * @param material
	 *            the material
	 * @param data
	 *            the byte data in intager form for lazies
	 */
	public void set(Location location, Material material, Integer data)
	{
		getSession(location.getWorld()).set(location, material, data);
	}
	
	/**
	 * Set the given location to a material:0
	 * 
	 * @param location
	 *            the location
	 * @param material
	 *            the material
	 */
	public void set(Location location, Material material)
	{
		getSession(location.getWorld()).set(location, material);
	}
	
	/**
	 * Make a pyramid
	 * 
	 * @param center
	 *            the center (bottom center)
	 * @param mb
	 *            the materialblock
	 * @param height
	 *            the height
	 * @param filled
	 *            should it be filled or hollow?
	 */
	public void makePyramid(Location center, MaterialBlock mb, int height, boolean filled)
	{
		getSession(center.getWorld()).makePyramid(center, mb, height, filled);
	}
	
	/**
	 * Make a pyramid
	 * 
	 * @param center
	 *            the center (bottom center)
	 * @param mb
	 *            the materialblock (multiple)
	 * @param height
	 *            the height
	 * @param filled
	 *            should it be filled or hollow?
	 */
	public void makePyramid(Location center, VariableBlock mb, int height, boolean filled)
	{
		getSession(center.getWorld()).makePyramid(center, mb, height, filled);
	}
	
	/**
	 * Make an elipsoid sphere
	 * 
	 * @param center
	 *            the center
	 * @param mb
	 *            the materialblock
	 * @param radiusX
	 *            the radius x
	 * @param radiusY
	 *            the radius y
	 * @param radiusZ
	 *            the radius z
	 * @param filled
	 *            filled or hollow?
	 */
	public void makeSphere(Location center, MaterialBlock mb, double radiusX, double radiusY, double radiusZ, boolean filled)
	{
		getSession(center.getWorld()).makeSphere(center, mb, radiusX, radiusY, radiusZ, filled);
	}
	
	/**
	 * Make an elipsoid sphere
	 * 
	 * @param center
	 *            the center
	 * @param mb
	 *            the materialblock (multiple)
	 * @param radiusX
	 *            the radius x
	 * @param radiusY
	 *            the radius y
	 * @param radiusZ
	 *            the radius z
	 * @param filled
	 *            filled or hollow?
	 */
	public void makeSphere(Location center, VariableBlock mb, double radiusX, double radiusY, double radiusZ, boolean filled)
	{
		getSession(center.getWorld()).makeSphere(center, mb, radiusX, radiusY, radiusZ, filled);
	}
	
	/**
	 * Create a sphere
	 * 
	 * @param center
	 *            the center
	 * @param mb
	 *            the materialblock
	 * @param radius
	 *            the radius
	 * @param filled
	 *            filled or not?
	 */
	public void makeSphere(Location center, MaterialBlock mb, double radius, boolean filled)
	{
		getSession(center.getWorld()).makeSphere(center, mb, radius, filled);
	}
	
	/**
	 * Create a sphere
	 * 
	 * @param center
	 *            the center
	 * @param mb
	 *            the materialblock (multiple)
	 * @param radius
	 *            the radius
	 * @param filled
	 *            filled or not?
	 */
	public void makeSphere(Location center, VariableBlock mb, double radius, boolean filled)
	{
		getSession(center.getWorld()).makeSphere(center, mb, radius, filled);
	}
	
	/**
	 * Make a cylinder
	 * 
	 * @param center
	 *            the center
	 * @param mb
	 *            the materialblock
	 * @param radius
	 *            the radius
	 * @param height
	 *            the height
	 * @param filled
	 *            filled or not
	 */
	public void makeCylinder(Location center, MaterialBlock mb, double radius, int height, boolean filled)
	{
		getSession(center.getWorld()).makeCylinder(center, mb, radius, height, filled);
	}
	
	/**
	 * Make a cylinder
	 * 
	 * @param center
	 *            the center
	 * @param mb
	 *            the materialblock (multiple)
	 * @param radius
	 *            the radius
	 * @param height
	 *            the height
	 * @param filled
	 *            filled or not
	 */
	public void makeCylinder(Location center, VariableBlock mb, double radius, int height, boolean filled)
	{
		getSession(center.getWorld()).makeCylinder(center, mb, radius, height, filled);
	}
	
	/**
	 * Get the biome at the given location (y is ignored)
	 * 
	 * @param location
	 *            the location
	 * @return the biome
	 */
	public Biome getBiome(Location location)
	{
		return getSession(location.getWorld()).getBiome(location.getBlockX(), location.getBlockZ());
	}
	
	/**
	 * Set the biome at the given location (y is ignored). Note, players may
	 * have to leave the area and come back (or relog) to see the changes.
	 * 
	 * @param location
	 *            the location
	 * @param biome
	 *            the biome
	 */
	public void setBiome(Location location, Biome biome)
	{
		getSession(location.getWorld()).setBiome(location.getBlockX(), location.getBlockZ(), biome);
	}
	
	/**
	 * Quickly get the highest block at the given location (y is ignored)
	 * 
	 * @param location
	 *            the location
	 * @return the highest level of natural blocks
	 */
	public int getHighestBlock(Location location)
	{
		return getSession(location.getWorld()).getHighestBlock(location.getBlockX(), location.getBlockZ());
	}
	
	/**
	 * Quickly get a block at the given location
	 * 
	 * @param location
	 *            the location
	 * @return the materialblock
	 */
	public MaterialBlock get(Location location)
	{
		return getSession(location.getWorld()).get(location);
	}
	
	/**
	 * Flush all used sessions in this queue and apply changes
	 */
	public void flush()
	{
		for(String i : sessions.k())
		{
			sessions.get(i).flush();
		}
	}
}

package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.phantomapi.lang.GMap;

/**
 * A world queue for all worlds
 * 
 * @author cyberpwn
 */
public class PhantomWorldQueue
{
	private GMap<String, PhantomEditSession> sessions;
	
	public PhantomWorldQueue()
	{
		sessions = new GMap<String, PhantomEditSession>();
	}
	
	public PhantomEditSession getSession(World world)
	{
		if(!sessions.containsKey(world.getName()))
		{
			sessions.put(world.getName(), new PhantomEditSession(new PhantomWorld(world)));
		}
		
		return sessions.get(world.getName());
	}
	
	public void set(Location location, MaterialBlock block)
	{
		getSession(location.getWorld()).set(location, block);
	}
	
	public void set(Location location, Material material, Byte data)
	{
		getSession(location.getWorld()).set(location, material, data);
	}
	
	public void set(Location location, Material material, Integer data)
	{
		getSession(location.getWorld()).set(location, material, data);
	}
	
	public void set(Location location, Material material)
	{
		getSession(location.getWorld()).set(location, material);
	}
	
	public void makePyramid(Location center, MaterialBlock mb, int height, boolean filled)
	{
		getSession(center.getWorld()).makePyramid(center, mb, height, filled);
	}
	
	public void makePyramid(Location center, VariableBlock mb, int height, boolean filled)
	{
		getSession(center.getWorld()).makePyramid(center, mb, height, filled);
	}
	
	public void makeSphere(Location center, MaterialBlock mb, double radiusX, double radiusY, double radiusZ, boolean filled)
	{
		getSession(center.getWorld()).makeSphere(center, mb, radiusX, radiusY, radiusZ, filled);
	}
	
	public void makeSphere(Location center, VariableBlock mb, double radiusX, double radiusY, double radiusZ, boolean filled)
	{
		getSession(center.getWorld()).makeSphere(center, mb, radiusX, radiusY, radiusZ, filled);
	}
	
	public void makeSphere(Location center, MaterialBlock mb, double radius, boolean filled)
	{
		getSession(center.getWorld()).makeSphere(center, mb, radius, filled);
	}
	
	public void makeSphere(Location center, VariableBlock mb, double radius, boolean filled)
	{
		getSession(center.getWorld()).makeSphere(center, mb, radius, filled);
	}
	
	public void makeCylinder(Location center, MaterialBlock mb, double radius, int height, boolean filled)
	{
		getSession(center.getWorld()).makeCylinder(center, mb, radius, height, filled);
	}
	
	public void makeCylinder(Location center, VariableBlock mb, double radius, int height, boolean filled)
	{
		getSession(center.getWorld()).makeCylinder(center, mb, radius, height, filled);
	}
	
	public Biome getBiome(Location location)
	{
		return getSession(location.getWorld()).getBiome(location.getBlockX(), location.getBlockZ());
	}
	
	public void setBiome(Location location, Biome biome)
	{
		getSession(location.getWorld()).setBiome(location.getBlockX(), location.getBlockZ(), biome);
	}
	
	public int getHighestBlock(Location location)
	{
		return getSession(location.getWorld()).getHighestBlock(location.getBlockX(), location.getBlockZ());
	}
	
	public MaterialBlock get(Location location)
	{
		return getSession(location.getWorld()).get(location);
	}
	
	public void flush()
	{
		for(String i : sessions.k())
		{
			sessions.get(i).flush();
		}
	}
}

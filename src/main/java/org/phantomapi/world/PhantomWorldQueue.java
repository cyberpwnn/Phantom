package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

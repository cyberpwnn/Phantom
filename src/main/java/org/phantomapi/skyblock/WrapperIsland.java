package org.phantomapi.skyblock;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.phantomapi.ext.SkyblockPluginConnector;

/**
 * Wrapper island
 * 
 * @author cyberpwn
 */
public class WrapperIsland implements SkyblockIsland
{
	private Object is;
	private SkyblockPluginConnector c;
	
	public WrapperIsland(Object is) throws ClassNotFoundException
	{
		this.is = is;
		c = new SkyblockPluginConnector();
		
		if(!c.exists())
		{
			throw new ClassNotFoundException("Cannot init skyblock connector (skyblock is missing)");
		}
	}
	
	@Override
	public int getDistance()
	{
		try
		{
			return c.getDistance(is);
		}
		
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	@Override
	public long getCreatedDate()
	{
		try
		{
			return c.getCreatedDate(is);
		}
		
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	@Override
	public Location getCenter()
	{
		try
		{
			return c.getCenter(is);
		}
		
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public World getWorld()
	{
		return getCenter().getWorld();
	}
	
	@Override
	public boolean contains(Entity e)
	{
		return contains(e.getLocation());
	}
	
	@Override
	public boolean contains(Location l)
	{
		try
		{
			return c.islandContains(is, l);
		}
		
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public boolean contains(Chunk cx)
	{
		try
		{
			return c.islandContains(is, cx);
		}
		
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public boolean contains(Block b)
	{
		return contains(b.getLocation());
	}
	
	@Override
	public UUID getOwner()
	{
		try
		{
			return c.getOwner(is);
		}
		
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public List<UUID> getMembers()
	{
		try
		{
			return c.getMembers(is);
		}
		
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Object getRawIsland()
	{
		return is;
	}
}

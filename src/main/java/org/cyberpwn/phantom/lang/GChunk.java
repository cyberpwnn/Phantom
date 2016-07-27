package org.cyberpwn.phantom.lang;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * A Chunk object which is serializable
 * 
 * @author cyberpwn
 *
 */
public class GChunk implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Integer x;
	private Integer z;
	private String world;
	
	/**
	 * Create a gchunk from an existing chunk
	 * 
	 * @param chunk
	 */
	public GChunk(Chunk chunk)
	{
		this(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
	}
	
	/**
	 * Get a GChunk from an existing location
	 * 
	 * @param location
	 */
	public GChunk(Location location)
	{
		this(location.getChunk().getX(), location.getChunk().getZ(), location.getChunk().getWorld().getName());
	}
	
	/**
	 * Create a GChunk from data
	 * 
	 * @param x
	 *            the x
	 * @param z
	 *            the z
	 * @param world
	 *            the world (NAME STRING)
	 */
	public GChunk(int x, int z, String world)
	{
		this.x = x;
		this.z = z;
		this.world = world;
	}
	
	/**
	 * Is the gchunk equal to another gchunk
	 */
	public boolean equals(Object o)
	{
		if(o != null)
		{
			if(o instanceof GChunk)
			{
				GChunk gc = (GChunk) o;
				
				if(this.x == gc.x && this.z == gc.z && this.world.equals(gc.world))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Is this chunk equal to an actual chunk
	 * 
	 * @param c
	 *            the chunk
	 * @return true if they are the same place
	 */
	public boolean isChunk(Chunk c)
	{
		if(this.x == c.getX() && this.z == c.getZ() && this.world.equals(c.getWorld().getName()))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Get the x
	 * 
	 * @return x
	 */
	public Integer getX()
	{
		return x;
	}
	
	/**
	 * Set the x
	 * 
	 * @param x
	 *            the x
	 */
	public void setX(Integer x)
	{
		this.x = x;
	}
	
	/**
	 * Get the z
	 * 
	 * @return the z
	 */
	public Integer getZ()
	{
		return z;
	}
	
	/**
	 * Set the z
	 * 
	 * @param z
	 *            the z
	 */
	public void setZ(Integer z)
	{
		this.z = z;
	}
	
	/**
	 * Get the world
	 * 
	 * @return the world name
	 */
	public String getWorld()
	{
		return world;
	}
	
	/**
	 * Set the world
	 * 
	 * @param world
	 *            the world name
	 */
	public void setWorld(String world)
	{
		this.world = world;
	}
	
	/**
	 * Get a chunk object out of this gchunk
	 * 
	 * @return the chunk
	 */
	public Chunk toChunk()
	{
		return Bukkit.getServer().getWorld(world).getChunkAt(x, z);
	}
	
	/**
	 * String rep
	 */
	public String toString()
	{
		return "Chunk: " + world + " @ [" + x + "," + z + "]";
	}
}

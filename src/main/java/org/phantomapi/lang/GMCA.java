package org.phantomapi.lang;

import java.io.Serializable;

/**
 * An MCA object which is serializable
 * 
 * @author cyberpwn
 */
public class GMCA implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Integer x;
	private Integer z;
	private String world;
	
	/**
	 * Create a GMCA from data
	 * 
	 * @param x
	 *            the x
	 * @param z
	 *            the z
	 * @param world
	 *            the world (NAME STRING)
	 */
	public GMCA(int x, int z, String world)
	{
		this.x = x;
		this.z = z;
		this.world = world;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((z == null) ? 0 : z.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		GMCA other = (GMCA) obj;
		
		if(world == null)
		{
			if(other.world != null)
			{
				return false;
			}
		}
		
		else if(!world.equals(other.world))
		{
			return false;
		}
		
		if(x == null)
		{
			if(other.x != null)
			{
				return false;
			}
		}
		
		else if(!x.equals(other.x))
		{
			return false;
		}
		
		if(z == null)
		{
			if(other.z != null)
			{
				return false;
			}
		}
		
		else if(!z.equals(other.z))
		{
			return false;
		}
		
		return true;
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
	 * String rep
	 */
	@Override
	public String toString()
	{
		return "MCA: " + world + " @ [" + x + "," + z + "]";
	}
}

package org.phantomapi.lang;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Serializable generic location object
 * 
 * @author cyberpwn
 */
public class GLocation implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private double x;
	private double y;
	private double z;
	private int blockX;
	private int blockY;
	private int blockZ;
	private float yaw;
	private float pitch;
	private String world;
	
	/**
	 * Get a GLocation from a location
	 * 
	 * @param location
	 *            the location
	 */
	public GLocation(Location location)
	{
		x = location.getX();
		y = location.getY();
		z = location.getZ();
		blockX = location.getBlockX();
		blockY = location.getBlockY();
		blockZ = location.getBlockZ();
		yaw = location.getYaw();
		pitch = location.getPitch();
		world = location.getWorld().getName();
	}
	
	/**
	 * Get the location object from this GLocation object
	 * 
	 * @return the location
	 */
	public Location toLocation()
	{
		if(Bukkit.getServer().getWorld(world) == null)
		{
			world = Bukkit.getServer().getWorlds().get(0).getName();
		}
		
		return new Location(Bukkit.getServer().getWorld(world), x, y, z, yaw, pitch);
	}
	
	/**
	 * Get the x
	 * 
	 * @return the x
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Set the x
	 * 
	 * @param x
	 *            the x
	 */
	public void setX(double x)
	{
		this.x = x;
	}
	
	/**
	 * Get the y
	 * 
	 * @return the y
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Set the y
	 * 
	 * @param y
	 *            the y
	 */
	public void setY(double y)
	{
		this.y = y;
	}
	
	/**
	 * Get the z
	 * 
	 * @return the z
	 */
	public double getZ()
	{
		return z;
	}
	
	/**
	 * Set the z
	 * 
	 * @param z
	 *            the z
	 */
	public void setZ(double z)
	{
		this.z = z;
	}
	
	/**
	 * Get the block x
	 * 
	 * @return the block x
	 */
	public int getBlockX()
	{
		return blockX;
	}
	
	/**
	 * Get the block y
	 * 
	 * @return the block y
	 */
	public int getBlockY()
	{
		return blockY;
	}
	
	/**
	 * Get the block z
	 * 
	 * @return the block z
	 */
	public int getBlockZ()
	{
		return blockZ;
	}
	
	/**
	 * Get the yaw <- ->
	 * 
	 * @return the yaw <- ->
	 */
	public float getYaw()
	{
		return yaw;
	}
	
	/**
	 * Set the yaw <- ->
	 * 
	 * @param yaw
	 *            the yaw <- ->
	 */
	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}
	
	/**
	 * Get the pitch ^
	 * 
	 * @return the pitch ^
	 */
	public float getPitch()
	{
		return pitch;
	}
	
	/**
	 * Set the pitch ^
	 * 
	 * @param pitch
	 *            the pitch ^
	 */
	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}
	
	/**
	 * Get the world name
	 * 
	 * @return the world name
	 */
	public String getWorld()
	{
		return world;
	}
	
	/**
	 * Set the world name
	 * 
	 * @param world
	 *            the world name
	 */
	public void setWorld(String world)
	{
		this.world = world;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + blockX;
		result = prime * result + blockY;
		result = prime * result + blockZ;
		result = prime * result + Float.floatToIntBits(pitch);
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(yaw);
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		
		GLocation other = (GLocation) obj;
		
		if(blockX != other.blockX)
		{
			return false;
		}
		
		if(blockY != other.blockY)
		{
			return false;
		}
		
		if(blockZ != other.blockZ)
		{
			return false;
		}
		
		if(Float.floatToIntBits(pitch) != Float.floatToIntBits(other.pitch))
		{
			return false;
		}
		
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
		
		if(Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
		{
			return false;
		}
		
		if(Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
		{
			return false;
		}
		
		if(Float.floatToIntBits(yaw) != Float.floatToIntBits(other.yaw))
		{
			return false;
		}
		
		if(Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
		{
			return false;
		}
		
		return true;
	}
}

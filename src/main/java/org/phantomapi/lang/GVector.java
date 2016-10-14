package org.phantomapi.lang;

import java.io.Serializable;
import org.bukkit.util.Vector;

/**
 * Serializable generic location object
 * 
 * @author cyberpwn
 */
public class GVector implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private double x;
	private double y;
	private double z;
	private int blockX;
	private int blockY;
	private int blockZ;
	
	/**
	 * Get a GVector from a vector
	 * 
	 * @param location
	 *            the location
	 */
	public GVector(Vector vector)
	{
		x = vector.getX();
		y = vector.getY();
		z = vector.getZ();
		blockX = vector.getBlockX();
		blockY = vector.getBlockY();
		blockZ = vector.getBlockZ();
	}
	
	/**
	 * Get the vector object from this GVector object
	 * 
	 * @return the vec
	 */
	public Vector toVector()
	{
		return new Vector(x, y, z);
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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + blockX;
		result = prime * result + blockY;
		result = prime * result + blockZ;
		long temp;
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
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
		
		GVector other = (GVector) obj;
		
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
		
		if(Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
		{
			return false;
		}
		
		if(Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
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

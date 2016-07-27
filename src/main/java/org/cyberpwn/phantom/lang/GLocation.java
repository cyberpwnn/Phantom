package org.cyberpwn.phantom.lang;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Serializable generic location object
 * 
 * @author cyberpwn
 *
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
}

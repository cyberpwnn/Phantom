package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Represents a target
 * 
 * @author cyberpwn
 */
public class WraithTarget
{
	private Location location;
	private Entity entity;
	
	/**
	 * Create a location based target
	 * 
	 * @param location
	 *            the location
	 */
	public WraithTarget(Location location)
	{
		this.location = location;
		this.entity = null;
	}
	
	/**
	 * Set a wraith target for an entity
	 * 
	 * @param entity
	 *            the entity target
	 */
	public WraithTarget(Entity entity)
	{
		this.location = null;
		this.entity = entity;
	}
	
	/**
	 * Set a wraith target for an entity
	 * 
	 * @param entity
	 *            the entity target
	 */
	public void setTarget(Entity entity)
	{
		this.entity = entity;
		this.location = null;
	}
	
	/**
	 * Set a location based target
	 * 
	 * @param location
	 *            the location
	 */
	public void setTarget(Location location)
	{
		this.entity = null;
		this.location = location;
	}
	
	/**
	 * Get the target for this wraith
	 * 
	 * @return the target
	 */
	public Location getTarget()
	{
		if(location != null)
		{
			return location;
		}
		
		else if(entity.isDead())
		{
			return null;
		}
		
		return entity.getLocation();
	}
}

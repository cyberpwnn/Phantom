package org.phantomapi.entity;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.phantomapi.physics.PhysicalObject;

/**
 * A Blanked Entity
 * 
 * @author cyberpwn
 */
public class BlankEntity extends PhysicalObject
{
	private String name;
	
	public BlankEntity(Location location, Vector volume)
	{
		super(location, volume);
		
		this.name = "";
	}
	
	/**
	 * Get the name of the entity
	 * 
	 * @return the name. Returns an empty string if not defined
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Set the name of the entity
	 * 
	 * @param name
	 *            the name of the entity.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}

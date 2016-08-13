package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * An artifact object
 * 
 * @author cyberpwn
 *
 */
public interface Artifact
{
	/**
	 * Build the object
	 */
	public void build();
	
	/**
	 * Move the artifact
	 * 
	 * @param location
	 *            the destination
	 */
	public void move(Location location);
	
	/**
	 * Move the artifact
	 * 
	 * @param vec
	 *            the destination
	 */
	public void move(Vector vec);
	
	/**
	 * Clear the object from the world
	 */
	public void clear();
	
	/**
	 * Get the location
	 * 
	 * @return the location
	 */
	public Location getLocation();
	
	/**
	 * Get the center of the object
	 * 
	 * @return the center of the object
	 */
	public Location getCenter();
	
	/**
	 * Get a cuboid of the object
	 * 
	 * @return the cuboid
	 */
	public Cuboid toCuboid();
	
	/**
	 * Is the object built?
	 * 
	 * @return true if built
	 */
	public Boolean isBuilt();
}

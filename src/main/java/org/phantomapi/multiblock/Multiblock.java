package org.phantomapi.multiblock;

import java.io.Serializable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;

/**
 * Represents a multiblock instance
 * 
 * @author cyberpwn
 */
public interface Multiblock extends Serializable
{
	/**
	 * Get all chunks this instance requires
	 * 
	 * @return the chunks
	 */
	public GList<Chunk> getChunks();
	
	/**
	 * Get the vector mapping for this structure
	 * 
	 * @return the mapping of structure vectors to literal locations
	 */
	public GMap<Vector, Location> getMapping();
	
	/**
	 * Get all locations for this structure
	 * 
	 * @return the structure
	 */
	public GList<Location> getLocations();
	
	/**
	 * Get the type of this structure
	 * 
	 * @return the type
	 */
	public String getType();
	
	/**
	 * Get the world
	 * 
	 * @return the world
	 */
	public World getWorld();
	
	/**
	 * Does the given location reside in this structure
	 * 
	 * @param location
	 *            the location
	 * @return true if it does
	 */
	public boolean contains(Location location);
	
	/**
	 * Get the size of this structure
	 * 
	 * @return the size
	 */
	public int size();
	
	/**
	 * Get the instance id for this structure
	 * 
	 * @return the id
	 */
	public int getId();
	
	/**
	 * Unload all responsible chunks for holding this structure
	 */
	public void unload();
	
	/**
	 * Load all responsible chunks for holding this structure
	 */
	public void load();
}

package org.phantomapi.game;

import org.bukkit.Location;
import org.phantomapi.world.ChunkletMesh;

/**
 * Region instance
 * 
 * @author cyberpwn
 *
 */
public interface Region extends PlayerContainer
{
	/**
	 * Get the map
	 * 
	 * @return the map
	 */
	public Map getMap();
	
	/**
	 * Get the mesh of chunklets for this region
	 * 
	 * @return the chunklet mesh
	 */
	public ChunkletMesh getMesh();
	
	/**
	 * Does the region contain a location?
	 * 
	 * @param l
	 *            the location
	 * @return true if the region contains the location
	 */
	public boolean contains(Location l);
}

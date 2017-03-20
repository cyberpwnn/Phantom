package org.phantomapi.world;

import org.bukkit.Location;

/**
 * Basic brush
 * 
 * @author cyberpwn
 */
public interface Brush
{
	/**
	 * Brush at the given location
	 * 
	 * @param l
	 *            the location
	 * @return the result
	 */
	public MaterialBlock get(Location l);
}
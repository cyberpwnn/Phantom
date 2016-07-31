package org.cyberpwn.phantom.game;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;

public interface RegionedMap
{
	/**
	 * Get all regions in this regioned map
	 * 
	 * @return the regions
	 */
	public GList<Region> getRegions();
	
	/**
	 * Add a region to this map
	 * 
	 * @param r
	 *            the region
	 */
	public void add(Region r);
	
	/**
	 * Get the region for this location
	 * 
	 * @param l
	 *            the location
	 * @return the region or null if not exists
	 */
	public Region getRegion(Location l);
	
	/**
	 * Get the region for this entity
	 * 
	 * @param e
	 *            the entity
	 * @return the region or null if not in a region
	 */
	public Region getRegion(Entity e);
	
	/**
	 * Get the region of this player
	 * 
	 * @param p
	 *            the player
	 * @return the region or null if not a region
	 */
	public Region getRegion(Player p);
	
	/**
	 * Get the region of this player
	 * 
	 * @param p
	 *            the player
	 * @return the region or null if not a region
	 */
	public Region getRegion(GamePlayer p);
}

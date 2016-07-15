package org.cyberpwn.phantom.game;

import org.cyberpwn.phantom.lang.GList;

/**
 * Game Regioned for maps
 * 
 * @author cyberpwn
 *
 * @param <M>
 *            The MAP TYPE (this)
 * @param <G>
 *            The GAME TYPE
 * @param <T>
 *            The TEAM TYPE
 * @param <P>
 *            The PLAYER OBJECT TYPE
 */
public interface RegionedMap<R extends Region<R, M, G, T, P>, M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>>
{
	/**
	 * Get all regions
	 * 
	 * @return list of regions
	 */
	public GList<R> getRegions();
	
	/**
	 * Add region
	 * 
	 * @param r
	 *            the region
	 */
	public void addRegion(R r);
	
	/**
	 * Contains region?
	 * 
	 * @param r
	 *            the region
	 * @return true if it exists
	 */
	public boolean contains(R r);
}

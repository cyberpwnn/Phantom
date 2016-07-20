package org.cyberpwn.phantom.game;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * A Game Map instance
 * 
 * @author cyberpwn
 *
 * @param <M>
 *            The MAP TYPE (this implementation class)
 * @param <G>
 *            The GAME TYPE
 * @param <T>
 *            The TEAM TYPE
 * @param <P>
 *            The PLAYER OBJECT TYPE
 */
public interface GameMap<M, G, T, P>
{
	/**
	 * Does the map contain this location
	 * 
	 * @param location
	 *            the location
	 * @return true if the map contains the location
	 */
	public boolean contains(Location location);
	
	/**
	 * The world this map resides in
	 * 
	 * @return the world
	 */
	public World getWorld();
	
	/**
	 * Load the map from a file
	 * 
	 * @param f
	 *            the file
	 */
	public void load(File f);
}

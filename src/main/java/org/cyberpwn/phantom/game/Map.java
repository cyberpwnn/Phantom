package org.cyberpwn.phantom.game;

import java.io.File;

import org.bukkit.World;

/**
 * A Map instance
 * 
 * @author cyberpwn
 *
 */
public interface Map extends PlayerContainer
{
	/**
	 * Load map
	 * 
	 * @param file
	 *            the data file
	 */
	public void load(File file);
	
	/**
	 * Get the game
	 * 
	 * @return the game
	 */
	public Game getGame();
	
	/**
	 * Set the binding game
	 * 
	 * @param game
	 *            the game
	 */
	public void setGame(Game game);
	
	/**
	 * Get the world this map is in
	 * 
	 * @return the world
	 */
	public World getWorld();
}

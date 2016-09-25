package org.phantomapi.game;

import org.phantomapi.lang.GMap;

/**
 * Represents a game state
 * 
 * @author cyberpwn
 */
public interface GameState
{
	/**
	 * Get the game
	 * 
	 * @return the game instance
	 */
	public Game getGame();
	
	/**
	 * Get all game objects
	 * 
	 * @return the game objects
	 */
	public GMap<String, GMap<String, GameObject>> getObjects();
	
	/**
	 * Get all game objects from the given type
	 * 
	 * @param type
	 *            the type
	 * @return the game objects or null
	 */
	public GMap<String, GameObject> getObjects(String type);
}

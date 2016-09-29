package org.phantomapi.game;

import org.bukkit.entity.Player;
import org.phantomapi.lang.GMap;

/**
 * Represents a game state
 * 
 * @author cyberpwn
 */
public interface GameState extends PlayerContainer, GameObjectContainer
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
	public GMap<String, GameObject> getObjects();
	
	/**
	 * Used for adding objects into the reference map
	 * 
	 * @param key
	 *            the key
	 * @param object
	 *            the object
	 */
	public void registerGameObject(String key, GameObject object);
	
	/**
	 * Add a player
	 * 
	 * @param p
	 *            the player
	 */
	public void addPlayer(Player p);
	
	/**
	 * Remove a player
	 * 
	 * @param p
	 *            the player
	 */
	public void removePlayer(Player p);
}

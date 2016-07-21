package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;

/**
 * Game player instance
 * 
 * @author cyberpwn
 *
 */
public interface GamePlayer
{
	/**
	 * Get the player instance
	 * 
	 * @return the player
	 */
	public Player getPlayer();
	
	/**
	 * Get the game
	 * 
	 * @return the game
	 */
	public Game getGame();
	
	/**
	 * Get the map
	 * 
	 * @return the map
	 */
	public Map getMap();
}

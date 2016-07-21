package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;

/**
 * A Game instance
 * 
 * @author cyberpwn
 *
 */
public interface Game extends PlayerContainer
{
	/**
	 * Get the game map
	 * 
	 * @return the map
	 */
	public Map getMap();
	
	/**
	 * Get the game player
	 * 
	 * @param p
	 *            the player
	 * @return the game player
	 */
	public GamePlayer getPlayer(Player p);
}

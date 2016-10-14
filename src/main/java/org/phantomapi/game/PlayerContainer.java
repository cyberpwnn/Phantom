package org.phantomapi.game;

import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;

/**
 * Contains players
 * 
 * @author cyberpwn
 */
public interface PlayerContainer
{
	/**
	 * Get players in this container
	 * 
	 * @return the players
	 */
	public GList<Player> getPlayers();
	
	/**
	 * Does the container contain the given player?
	 * 
	 * @param p
	 *            the player
	 * @return true if it does
	 */
	public boolean contains(Player p);
}

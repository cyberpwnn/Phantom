package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;

/**
 * Something that contains game players
 * 
 * @author cyberpwn
 *
 */
public interface PlayerContainer
{
	/**
	 * Get the players within this container
	 * 
	 * @return the players
	 */
	public GList<GamePlayer> getPlayers();
	
	/**
	 * Does this container have the specified player?
	 * 
	 * @param p
	 *            the player
	 * @return true if the player resides within this container
	 */
	public boolean contains(GamePlayer p);
	
	/**
	 * Does this container have the specified player?
	 * 
	 * @param p
	 *            the player
	 * @return true if the player resides within this container
	 */
	public boolean contains(Player p);
}

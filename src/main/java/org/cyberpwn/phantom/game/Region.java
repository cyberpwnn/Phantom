package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;

/**
 * Game Region
 * 
 * @author cyberpwn
 *
 * @param <M>
 *            The MAP TYPE
 * @param <G>
 *            The GAME TYPE
 * @param <T>
 *            The TEAM TYPE
 * @param <P>
 *            The PLAYER OBJECT TYPE
 */
public interface Region<R extends Region<R, M, G, T, P>, M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>>
{
	/**
	 * Get the map
	 * 
	 * @return the map
	 */
	public M getMap();
	
	/**
	 * Contains P player object
	 * 
	 * @param player
	 *            the player
	 * @return true if player is in there
	 */
	public boolean contains(P player);
	
	/**
	 * Contains Player object
	 * 
	 * @param player
	 *            the player
	 * @return true if player is in there
	 */
	public boolean contains(Player player);
}

package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;

/**
 * Game Team
 * 
 * @author cyberpwn
 *
 * @param <M>
 *            The MAP TYPE
 * @param <G>
 *            The GAME TYPE
 * @param <T>
 *            The TEAM TYPE (THIS)
 * @param <P>
 *            The PLAYER OBJECT TYPE
 */
public interface Team<M, G, T, P> extends Colored
{
	/**
	 * Get team name
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Get players in team
	 * 
	 * @return the players
	 */
	public GList<Player> getPlayers();
	
	/**
	 * Get game players in team
	 * 
	 * @return the players
	 */
	public GList<P> getGamePlayers();
	
	/**
	 * Get the game
	 * 
	 * @return the game
	 */
	public G getGame();
	
	/**
	 * Get player size
	 * 
	 * @return the size
	 */
	public Integer size();
	
	/**
	 * Contains player object
	 * 
	 * @param p
	 *            player object
	 * @return true if contains
	 */
	public Boolean contains(P p);
	
	/**
	 * Contains player
	 * 
	 * @param p
	 *            player
	 * @return true if contains
	 */
	public Boolean contains(Player p);
	
	/**
	 * Add player object to team
	 * 
	 * @param p
	 *            the player object
	 */
	public void add(P p);
	
	/**
	 * Remove player object from team
	 * 
	 * @param p
	 *            the player object
	 */
	public void remove(P p);
}

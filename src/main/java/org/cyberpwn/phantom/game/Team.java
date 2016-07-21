package org.cyberpwn.phantom.game;

import org.cyberpwn.phantom.util.C;

/**
 * Team instance
 * 
 * @author cyberpwn
 *
 */
public interface Team extends PlayerContainer
{
	/**
	 * Get the team color
	 * 
	 * @return the color
	 */
	public C getColor();
	
	/**
	 * Get the name
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Add a player to the team
	 * 
	 * @param player
	 *            the player
	 */
	public void add(GamePlayer player);
	
	/**
	 * Remove a player from the team
	 * 
	 * @param player
	 *            the player
	 */
	public void remove(GamePlayer player);
}

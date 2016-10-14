package org.phantomapi.currency;

import org.bukkit.entity.Player;

/**
 * General currency interface
 * 
 * @author cyberpwn
 */
public interface Currency
{
	/**
	 * Get the amount of currency the given player has
	 * 
	 * @param p
	 *            the player
	 * @return the double amount
	 */
	public double get(Player p);
	
	/**
	 * Give the player some currency
	 * 
	 * @param p
	 *            the player
	 * @param amt
	 *            the amount
	 */
	public void give(Player p, double amt);
	
	/**
	 * Take currency from a player
	 * 
	 * @param p
	 *            the player
	 * @param amt
	 *            the amt
	 */
	public void take(Player p, double amt);
	
	/**
	 * Get the suffix.
	 * 
	 * @return returns "" or a suffix
	 */
	public String getSuffix();
	
	/**
	 * Get the prefix.
	 * 
	 * @return returns "" or a prefix
	 */
	public String getPrefix();
}

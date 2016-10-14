package org.phantomapi.currency;

import org.bukkit.entity.Player;

/**
 * Handles currency diff messages
 * 
 * @author cyberpwn
 */
public interface CurrencyMessager
{
	/**
	 * Currency message earned
	 * 
	 * @param p
	 *            the player
	 * @param amt
	 *            the amount earned
	 * @param total
	 *            the total
	 * @return the string diff
	 */
	public String earned(Player p, double amt, double total);
	
	/**
	 * Currency message spent
	 * 
	 * @param p
	 *            the player
	 * @param amt
	 *            the amount spent
	 * @param total
	 *            the total remaining
	 * @return the message diff
	 */
	public String spent(Player p, double amt, double total);
	
	/**
	 * The currenct amount
	 * 
	 * @param p
	 *            the player
	 * @param total
	 *            the total
	 * @return the message diff
	 */
	public String current(Player p, double total);
	
	/**
	 * Get the currency
	 * 
	 * @return the currency
	 */
	public Currency getCurrency();
}

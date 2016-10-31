package org.phantomapi.xp;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.currency.Transaction;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

/**
 * Experience utility
 * 
 * @author cyberpwn
 */
public class XP
{
	/**
	 * Get experience for the given player
	 * 
	 * @param p
	 *            the player
	 * @return the experience
	 */
	public static long get(Player p)
	{
		return (long) new XPCurrency().get(p);
	}
	
	/**
	 * Give the player experience
	 * 
	 * @param p
	 *            the player
	 * @param amt
	 *            the amt
	 */
	public static void give(Player p, long amt, String type)
	{
		new Transaction(new XPCurrency()).noDiff().to(p).amount((double) amt).commit();
		
		Notification n = new Notification();
		n.setTitle(new Title("", C.DARK_GRAY + "+ " + C.AQUA + C.BOLD + amt + C.RESET + C.AQUA + " XP", C.YELLOW + "+ " + F.pc(getBoost(p)) + " | " + type, 2, 4, 5));
		n.setAudible(new GSound(Sound.ORB_PICKUP, 1f, 1.5f));
		n.setPriority(Priority.LOW);
		Phantom.queueNotification(p, n);
	}
	
	/**
	 * Take experience
	 * 
	 * @param p
	 *            the player
	 * @param amt
	 *            the amt
	 */
	public static void take(Player p, long amt, String type)
	{
		new Transaction(new XPCurrency()).noDiff().from(p).amount((double) amt).commit();
	}
	
	/**
	 * Get the player's xp boost
	 * 
	 * @param p
	 *            the player
	 * @return the boost
	 */
	public static double getBoost(Player p)
	{
		return Phantom.instance().getXpController().getEdc().get(p).getBoost();
	}
	
	/**
	 * Set the player's xp boost
	 * 
	 * @param p
	 *            the player
	 * @param amt
	 *            the amt
	 */
	public static void setBoost(Player p, double amt)
	{
		Phantom.instance().getXpController().getEdc().get(p).setBoost(amt);
	}
	
	/**
	 * Add to a player boost
	 * 
	 * @param p
	 *            the player
	 * @param amt
	 *            the amt
	 */
	public static void addBoost(Player p, double amt)
	{
		setBoost(p, getBoost(p) + amt);
	}
	
	/**
	 * Take from the player boost
	 * 
	 * @param p
	 *            the player boost
	 * @param amt
	 *            the amt
	 */
	public static void takeBoost(Player p, double amt)
	{
		setBoost(p, getBoost(p) - amt);
	}
}

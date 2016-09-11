package org.phantomapi.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;

/**
 * Player utils
 * 
 * @author cyberpwn
 */
public class P
{
	/**
	 * Can you find a player with the search?
	 * 
	 * @param search
	 *            the search
	 * @return true if a player can be found
	 */
	public boolean canFindPlayer(String search)
	{
		return findPlayer(search) == null ? false : true;
	}
	
	/**
	 * Find a player
	 * 
	 * @param search
	 *            the search
	 * @return the player or null
	 */
	public Player findPlayer(String search)
	{
		for(Player i : onlinePlayers())
		{
			if(i.getName().equalsIgnoreCase(search))
			{
				return i;
			}
		}
		
		for(Player i : onlinePlayers())
		{
			if(i.getName().toLowerCase().contains(search.toLowerCase()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	/**
	 * Get online players
	 * 
	 * @return the players
	 */
	public static GList<Player> onlinePlayers()
	{
		return Phantom.instance().onlinePlayers();
	}
	
	/**
	 * Get the location of the player's crotch (it's needed sometimes)
	 * 
	 * @param p
	 *            the player
	 * @return playerjunk
	 */
	public static Location getCrotchLocation(Player p)
	{
		return p.getLocation().add(0, 0.899, 0).add(p.getLocation().getDirection().setY(0).multiply(0.1));
	}
	
	/**
	 * Clear maxhealth, potion effects, speed and more
	 * 
	 * @param p
	 *            the player
	 */
	public static void clear(Player p)
	{
		resetMaxHeath(p);
		resetHunger(p);
		heal(p);
	}
	
	/**
	 * Clear player potion effects
	 * 
	 * @param p
	 *            the player
	 */
	public static void clearEffects(Player p)
	{
		for(PotionEffect i : new GList<PotionEffect>(p.getActivePotionEffects()))
		{
			p.removePotionEffect(i.getType());
		}
	}
	
	/**
	 * Heal the player an amount
	 * 
	 * @param p
	 *            the player
	 * @param health
	 *            the health
	 */
	public static void heal(Player p, double health)
	{
		p.setHealth(p.getHealth() + health > p.getMaxHealth() ? p.getMaxHealth() : p.getHealth() + health);
	}
	
	/**
	 * Heal the player to max health
	 * 
	 * @param p
	 *            the player
	 */
	public static void heal(Player p)
	{
		p.setHealth(p.getHealth());
	}
	
	/**
	 * Reset the player max health
	 * 
	 * @param p
	 *            the player
	 */
	public static void resetMaxHeath(Player p)
	{
		p.setMaxHealth(20);
	}
	
	/**
	 * Resets the player hunger
	 * 
	 * @param p
	 *            the hunger
	 */
	public static void resetHunger(Player p)
	{
		p.setFoodLevel(20);
	}
	
	/**
	 * Kill the player
	 * 
	 * @param p
	 *            the player p
	 */
	public static void kill(Player p)
	{
		p.setHealth(0);
	}
}

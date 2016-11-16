package org.phantomapi.util;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.world.Area;
import org.phantomapi.world.RayTrace;
import org.phantomapi.world.Shape;

/**
 * Player utils
 * 
 * @author cyberpwn
 */
public class P
{
	/**
	 * Get the target block
	 * 
	 * @param p
	 *            the player
	 * @param distance
	 *            the max distance
	 * @return the location
	 */
	public static Location targetBlock(Player p, int distance)
	{
		return p.getTargetBlock((Set<Material>) null, distance).getLocation().clone().add(0.5, 0.5, 0.5);
	}
	
	/**
	 * Get the target entity the player is looking at
	 * 
	 * @param p
	 *            the player
	 * @param distance
	 *            the max distance
	 * @return the entity or null
	 */
	public static Entity targetEntity(Player p, int distance)
	{
		return getEntityLookingAt(p, distance, 1);
	}
	
	/**
	 * Does the player have an inventory open
	 * 
	 * @param player
	 *            the player
	 * @return true if the player does
	 */
	public static boolean hasInventoryOpen(final Player player)
	{
		final InventoryView view = player.getOpenInventory();
		return view != null && view.getType() != InventoryType.CRAFTING;
	}
	
	/**
	 * Get the target entity of the player
	 * 
	 * @param e
	 *            the player
	 * @param range
	 *            the max range
	 * @param off
	 *            the offset
	 * @return the entity or null
	 */
	public static Entity getEntityLookingAt(Player e, double range, double off)
	{
		if(off < 1)
		{
			off = 1;
		}
		
		if(range < 1)
		{
			range = 1;
		}
		
		final Double doff = off;
		final Entity[] result = new Entity[1];
		
		new RayTrace(e.getLocation().clone().add(0.5, 1.5, 0.5), e.getLocation().getDirection(), range, (double) 1)
		{
			@Override
			public void onTrace(Location l)
			{
				Area a = new Area(l, doff);
				
				for(Entity i : a.getNearbyEntities())
				{
					if(!e.equals(i))
					{
						stop();
						result[0] = i;
						return;
					}
				}
			}
		}.trace();
		
		return result[0];
	}
	
	/**
	 * Get total experience
	 * 
	 * @param p
	 *            the player
	 * @return the total xp
	 */
	public static int getTotalExperience(Player p)
	{
		try
		{
			Class<?> sef = Class.forName("com.earth2me.essentials.craftbukkit.SetExpFix");
			return (int) sef.getMethod("getTotalExperience", Player.class).invoke(null, p);
		}
		
		catch(Exception e)
		{
			
		}
		
		return -1;
	}
	
	/**
	 * Set total experience
	 * 
	 * @param p
	 *            the player
	 * @param xp
	 *            the xp
	 */
	public static void setTotalExperience(Player p, int xp)
	{
		try
		{
			Class<?> sef = Class.forName("com.earth2me.essentials.craftbukkit.SetExpFix");
			sef.getMethod("setTotalExperience", Player.class, int.class).invoke(null, p, xp);
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	/**
	 * Can you find a player with the search?
	 * 
	 * @param search
	 *            the search
	 * @return true if a player can be found
	 */
	public static boolean canFindPlayer(String search)
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
	public static Player findPlayer(String search)
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
	
	/**
	 * Get the area of the player in the form of a shape
	 * 
	 * @param p
	 *            the player
	 * @return the shape
	 */
	public static Shape getShape(Player p)
	{
		return new Shape(P.getCrotchLocation(p), new Vector(0.7, 1.8, 0.7));
	}
	
	/**
	 * Get the 1st person hand.
	 * 
	 * @param p
	 *            the player
	 * @return the estimate location of their hand
	 */
	public static Location getHand(Player p)
	{
		return getHand(p, 0f, 0f);
	}
	
	/**
	 * Get the 1st person hand.
	 * 
	 * @param p
	 *            the player
	 * @param yawShift
	 *            the shift yaw
	 * @param pitchShift
	 *            the shift pitch
	 * @return the location
	 */
	public static Location getHand(Player p, float yawShift, float pitchShift)
	{
		Location base = p.getEyeLocation();
		Location mode = p.getEyeLocation();
		Float yaw = p.getLocation().getYaw() + 50 + yawShift;
		Float pitch = p.getLocation().getPitch() + pitchShift;
		
		mode.setYaw(yaw);
		mode.setPitch(pitch);
		base.add(mode.getDirection());
		
		return base;
	}
	
	/**
	 * Show progress
	 * 
	 * @param p
	 *            player
	 * @param msg
	 *            the message
	 */
	public static void showProgress(Player p, String msg)
	{
		Phantom.instance().getDms().showProgress(p, msg);
	}
	
	/**
	 * Remove all progress for player
	 * 
	 * @param p
	 *            the player
	 */
	public static void clearProgress(Player p)
	{
		Phantom.instance().getDms().clearProgress(p);
	}
}

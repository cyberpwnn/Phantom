package org.phantomapi.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GListAdapter;
import org.phantomapi.world.Area;

/**
 * Player utils
 * 
 * @author cyberpwn
 */
public class Players
{
	/**
	 * Is the given player online?
	 * 
	 * @param player
	 *            the player
	 * @return true if the player is
	 */
	public static boolean isOnline(String player)
	{
		return getPlayer(player) != null;
	}
	
	/**
	 * Get the given player
	 * 
	 * @param player
	 *            the player name
	 * @return the player or null
	 */
	public static Player getPlayer(String player)
	{
		return Bukkit.getPlayer(player);
	}
	
	/**
	 * Get literally any player
	 * 
	 * @return any player or null if no players online
	 */
	public static Player getAnyPlayer()
	{
		return Phantom.instance().onlinePlayers().isEmpty() ? null : Phantom.instance().onlinePlayers().get(0);
	}
	
	/**
	 * Is there any player online?
	 * 
	 * @return true if at least one player is online
	 */
	public static boolean isAnyOnline()
	{
		return !Bukkit.getOnlinePlayers().isEmpty();
	}
	
	/**
	 * Get all players in the given world
	 * 
	 * @param world
	 *            the world
	 * @return the players
	 */
	public static GList<Player> inWorld(World world)
	{
		return new GList<Player>(world.getPlayers());
	}
	
	/**
	 * Get all players in the given chunk
	 * 
	 * @param chunk
	 *            the chunk
	 * @return the list of players
	 */
	public static GList<Player> inChunk(Chunk chunk)
	{
		return new GList<Player>(new GListAdapter<Entity, Player>()
		{
			@Override
			public Player onAdapt(Entity from)
			{
				if(from.getType().equals(EntityType.PLAYER))
				{
					return (Player) from;
				}
				
				return null;
			}
		}.adapt(new GList<Entity>(chunk.getEntities())));
	}
	
	/**
	 * Get all players in the given area
	 * 
	 * @param l
	 *            the center location
	 * @param radius
	 *            the three dimensional area radius to search
	 * @return a list of players in the given area
	 */
	public static GList<Player> inArea(Location l, double radius)
	{
		return new GList<Player>(new Area(l, radius).getNearbyPlayers());
	}
	
	/**
	 * Get all players in the given area
	 * 
	 * @param l
	 *            the center location
	 * @param radius
	 *            the three dimensional area radius to search
	 * @return a list of players in the given area
	 */
	public static GList<Player> inArea(Location l, int radius)
	{
		return new GList<Player>(new Area(l, radius).getNearbyPlayers());
	}
}

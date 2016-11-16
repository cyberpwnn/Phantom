package org.phantomapi.placeholder;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.PlaceholderAPI;

/**
 * Placeholder utils
 * 
 * @author cyberpwn
 */
public class PlaceholderUtil
{
	/**
	 * Handle
	 * 
	 * @param p
	 *            the player
	 * @param s
	 *            the string
	 * @return the placeholder result or null
	 */
	public static String handle(Player p, String s)
	{
		return PlaceholderAPI.setPlaceholders(p, s);
	}
}

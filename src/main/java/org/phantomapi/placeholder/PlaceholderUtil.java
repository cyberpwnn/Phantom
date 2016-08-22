package org.phantomapi.placeholder;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderUtil
{
	public static String handle(Player p, String s)
	{
		return PlaceholderAPI.setPlaceholders(p, s);
	}
}

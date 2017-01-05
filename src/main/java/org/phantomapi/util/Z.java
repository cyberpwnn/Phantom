package org.phantomapi.util;

import org.bukkit.entity.Player;
import org.phantomapi.Phantom;

/**
 * Zenith permissions
 * 
 * @author cyberpwn
 */
public class Z
{
	public static boolean isZenith(Player p)
	{
		return Phantom.instance().getZenithController().isZenith(p);
	}
	
	public static void setZenith(Player p, boolean tf)
	{
		Phantom.instance().getZenithController().setZenith(p, tf);
	}
}

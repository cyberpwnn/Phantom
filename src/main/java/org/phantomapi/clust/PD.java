package org.phantomapi.clust;

import org.bukkit.entity.Player;
import org.phantomapi.Phantom;

/**
 * Playerdata
 * 
 * @author cyberpwn
 */
public class PD
{
	/**
	 * Get playerdata
	 * 
	 * @param player
	 *            the player
	 * @return the playerdata
	 */
	public static PlayerData get(Player player)
	{
		return Phantom.instance().getPdm().get(player);
	}
}

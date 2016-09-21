package org.phantomapi.wraith;

import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import net.citizensnpcs.api.CitizensAPI;

/**
 * Wraith utils
 * 
 * @author cyberpwn
 */
public class WraithUtil
{
	/**
	 * Is the player a wraith or an actual player?
	 * 
	 * @param p
	 *            the player
	 * @return true if the player is a wraith
	 */
	public static boolean isWraith(Player p)
	{
		return CitizensAPI.getNPCRegistry().isNPC(p) && getWraith(p) != null;
	}
	
	/**
	 * Get the wraith from this player
	 * 
	 * @param p
	 *            the player
	 * @return the wraith
	 */
	public static Wraith getWraith(Player p)
	{
		return Phantom.instance().getWraithController().get(p.getEntityId());
	}
}

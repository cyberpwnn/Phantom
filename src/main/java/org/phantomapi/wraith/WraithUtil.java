package org.phantomapi.wraith;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

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
	
	/**
	 * Get the wraith from this npc
	 * 
	 * @param npc
	 *            the npc
	 * @return the wraith
	 */
	public static Wraith getWraith(NPC npc)
	{
		if(isWraith(npc))
		{
			return Phantom.instance().getWraithController().get(npc.getEntity().getEntityId());
		}
		
		return null;
	}
	
	/**
	 * Is the given npc a player, wraith and registered within the wraith
	 * controller?
	 * 
	 * @param npc
	 *            the npc
	 * @return true if it is a wraith
	 */
	public static boolean isWraith(NPC npc)
	{
		return npc.getEntity().getType().equals(EntityType.PLAYER) && isWraith((Player) npc.getEntity());
	}
}

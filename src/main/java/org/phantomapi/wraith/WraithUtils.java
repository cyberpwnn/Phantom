package org.phantomapi.wraith;

import org.bukkit.entity.Entity;
import net.citizensnpcs.api.CitizensAPI;

/**
 * Utils for wraiths
 * 
 * @author cyberpwn
 */
public class WraithUtils
{
	/**
	 * Is the given entity a wraith?
	 * 
	 * @param e
	 *            the entity
	 * @return true if it is
	 */
	public static boolean isWraith(Entity e)
	{
		return e.hasMetadata("NPC");
	}
	
	/**
	 * Get the wraith
	 * 
	 * @param e
	 *            the entity
	 * @return a wraith or null if non existant
	 */
	public static Wraith getWraith(Entity e)
	{
		if(!isWraith(e))
		{
			return null;
		}
		
		return new PhantomWraith(CitizensAPI.getNPCRegistry().getNPC(e).getId());
	}
}

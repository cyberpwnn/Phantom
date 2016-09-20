package org.phantomapi.wraith;

import org.bukkit.entity.Entity;
import org.phantomapi.util.D;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;

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
	
	/**
	 * Register traits
	 * 
	 * @param trait
	 */
	public static void registerTrait(Class<? extends Trait> trait)
	{
		try
		{
			CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(trait));
		}
		
		catch(Exception e)
		{
			new D("WraithAPI").w(e.getMessage() + " > " + trait);
		}
	}
}

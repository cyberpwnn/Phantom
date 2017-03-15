package org.phantomapi.hologram;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

/**
 * An armorstand hologram
 * 
 * @author cyberpwn
 */
public class ArmorStandHologram extends EntityHologram
{
	/**
	 * Create an invisible, non-gravity hologram at the given location
	 * 
	 * @param initialLocation
	 *            the initial location
	 */
	public ArmorStandHologram(Location initialLocation)
	{
		super(initialLocation.getWorld().spawnEntity(initialLocation, EntityType.ARMOR_STAND));
		
		ArmorStand a = (ArmorStand) getHandle();
		a.setGravity(false);
		a.teleport(initialLocation);
		a.setVisible(false);
	}
}

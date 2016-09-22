package org.phantomapi.wraith;

import org.bukkit.entity.Entity;

/**
 * Damagable handle
 * 
 * @author cyberpwn
 */
public interface DamagableHandle
{
	/**
	 * Called when an entity damages this wraith
	 * 
	 * @param damager
	 *            the damager
	 * @param damage
	 *            the damage
	 */
	public void onDamage(Entity damager, Double damage);
}

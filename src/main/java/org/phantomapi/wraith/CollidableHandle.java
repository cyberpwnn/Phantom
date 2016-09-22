package org.phantomapi.wraith;

import org.bukkit.entity.Entity;

/**
 * Collidable handle
 * 
 * @author cyberpwn
 */
public interface CollidableHandle
{
	/**
	 * Called when an entity collides with this wraith
	 * 
	 * @param e
	 *            the entity
	 */
	public void onCollide(Entity e);
}

package org.cyberpwn.phantom.phy;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Bind an entity to a physical object
 * 
 * @author cyberpwn
 *
 */
public class BoundPhysicalObject extends PhysicalObject
{
	private Entity e;
	
	/**
	 * Create a physical object bound to an entity (movable)
	 * 
	 * @param e
	 *            the entity
	 * @param volume
	 *            the volume
	 */
	public BoundPhysicalObject(Entity e, Vector volume)
	{
		super(e.getLocation(), volume);
		
		this.e = e;
	}
	
	/**
	 * Overrides to force the entities velocity instead of an internal value
	 */
	public void influenceForce(Vector v)
	{
		e.setVelocity(e.getVelocity().add(v));
	}
	
	/**
	 * Get the entity bound to this physical object
	 * 
	 * @return the bound entity
	 */
	public Entity getEntity()
	{
		return e;
	}
}

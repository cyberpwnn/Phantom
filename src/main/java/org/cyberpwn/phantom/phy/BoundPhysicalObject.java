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
	}
	
	public void influenceGravity(Physical p)
	{
		p.influenceGravity(this);
		Double distance = p.getLocation().distance(getLocation());
		Double force = ((mass * p.getMass()) / distance);
		Vector velocity = p.getLocation().subtract(location).toVector().normalize().multiply(force);
		influenceForce(velocity);
	}
	
	public void influenceForce(Vector v)
	{
		velocity.add(v);
	}
}

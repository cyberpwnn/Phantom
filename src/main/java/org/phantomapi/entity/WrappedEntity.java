package org.phantomapi.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.phantomapi.physics.Physical;

/**
 * A Wrapped entity
 * @author cyberpwn
 *
 */
public class WrappedEntity extends BlankEntity
{
	private Entity entity;
	
	public WrappedEntity(Entity entity)
	{
		super(entity.getLocation(), new Vector(1, 1, 1));
		
		this.entity = entity;
	}
	
	/**
	 * Get the entity being wrapped
	 * @return the entity
	 */
	public Entity getEntity()
	{
		return entity;
	}

	@Override
	public Location getLocation()
	{
		return entity.getLocation();
	}

	@Override
	public Vector getVelocity()
	{
		return entity.getVelocity();
	}

	@Override
	public void influenceGravity(Physical p)
	{
		super.influenceGravity(p);
		entity.setVelocity(entity.getVelocity().add(velocity));
	}

	@Override
	public void influenceForce(Vector v)
	{
		super.influenceForce(v);
		entity.setVelocity(entity.getVelocity().add(velocity));
	}
}

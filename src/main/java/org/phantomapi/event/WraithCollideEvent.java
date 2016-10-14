package org.phantomapi.event;

import org.bukkit.entity.Entity;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith collided with another entity
 * 
 * @author cyberpwn
 */
public class WraithCollideEvent extends WraithEvent
{
	private final Entity entity;
	
	public WraithCollideEvent(Wraith wraith, Entity entity)
	{
		super(wraith);
		
		this.entity = entity;
	}
	
	/**
	 * Get the colliding entity
	 * 
	 * @return the entity
	 */
	public Entity getEntity()
	{
		return entity;
	}
}

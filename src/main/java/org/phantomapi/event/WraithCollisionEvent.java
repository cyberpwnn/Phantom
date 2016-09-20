package org.phantomapi.event;

import org.bukkit.entity.Entity;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith collided with entity
 * 
 * @author cyberpwn
 */
public class WraithCollisionEvent extends WraithEvent
{
	private final Entity entity;
	
	public WraithCollisionEvent(Wraith wraith, Entity entity)
	{
		super(wraith);
		
		this.entity = entity;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
}

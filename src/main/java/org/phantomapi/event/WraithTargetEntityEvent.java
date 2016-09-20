package org.phantomapi.event;

import org.bukkit.entity.Entity;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith targeted an entity
 * 
 * @author cyberpwn
 */
public class WraithTargetEntityEvent extends WraithEvent
{
	private final Entity entity;
	
	public WraithTargetEntityEvent(Wraith wraith, Entity entity)
	{
		super(wraith);
		
		this.entity = entity;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
}

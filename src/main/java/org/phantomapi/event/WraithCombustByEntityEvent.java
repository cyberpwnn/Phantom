package org.phantomapi.event;

import org.bukkit.entity.Entity;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith was exploded by the given entity
 * 
 * @author cyberpwn
 */
public class WraithCombustByEntityEvent extends WraithEvent
{
	private final Entity entity;
	
	public WraithCombustByEntityEvent(Wraith wraith, Entity entity)
	{
		super(wraith);
		
		this.entity = entity;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
}

package org.phantomapi.event;

import org.bukkit.entity.Entity;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith was damaged by the given entity
 * 
 * @author cyberpwn
 */
public class WraithDamageByEntityEvent extends WraithEvent
{
	private final Entity entity;
	private final Double damage;
	
	public WraithDamageByEntityEvent(Wraith wraith, Entity entity, Double damage)
	{
		super(wraith);
		
		this.entity = entity;
		this.damage = damage;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public Double getDamage()
	{
		return damage;
	}
}

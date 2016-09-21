package org.phantomapi.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith was damaged
 * 
 * @author cyberpwn
 */
public class WraithDamageEvent extends WraithEvent
{
	private final Entity damager;
	private final DamageCause cause;
	private Double damage;
	
	public WraithDamageEvent(Wraith wraith, Entity damager, DamageCause cause, Double damage)
	{
		super(wraith);
		
		this.damager = damager;
		this.damage = damage;
		this.cause = cause;
	}
	
	/**
	 * Get damager
	 * 
	 * @return the entity
	 */
	public Entity getDamager()
	{
		return damager;
	}
	
	public DamageCause getCause()
	{
		return cause;
	}
	
	public Double getDamage()
	{
		return damage;
	}

	public void setDamage(Double damage)
	{
		this.damage = damage;
	}
}

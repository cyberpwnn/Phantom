package org.cyberpwn.phantom.event.structure;

import org.cyberpwn.phantom.util.Health;

/**
 * Represents a combat event
 * 
 * @author cyberpwn
 *
 */
public class CombatEvent extends CancellablePhantomEvent
{
	private Double damage;
	
	public CombatEvent(Double damage)
	{
		this.damage = damage;
	}
	
	/**
	 * Get the damage
	 * 
	 * @return the damage
	 */
	public Double getDamage()
	{
		return damage;
	}
	
	/**
	 * Set the damage
	 * 
	 * @param damage
	 *            the damage
	 */
	public void setDamage(Double damage)
	{
		this.damage = damage;
	}
	
	/**
	 * Multiply the damage by the given multiplier
	 * 
	 * @param multiplier
	 *            the multiplier
	 */
	public void multiplyDamage(Double multiplier)
	{
		damage *= multiplier;
	}
	
	/**
	 * Get a health object of hearts
	 * 
	 * @return the health
	 */
	public Health getDamageHearts()
	{
		return new Health(damage);
	}
}

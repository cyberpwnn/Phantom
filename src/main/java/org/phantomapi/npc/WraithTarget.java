package org.phantomapi.npc;

import org.bukkit.entity.Entity;

/**
 * Represents a wraith target
 * 
 * @author cyberpwn
 */
public class WraithTarget
{
	private Entity target;
	private Boolean aggressive;
	
	/**
	 * Create a wraith target
	 * 
	 * @param target
	 *            the entity
	 * @param aggressive
	 *            to be aggro?
	 */
	public WraithTarget(Entity target, Boolean aggressive)
	{
		this.target = target;
		this.aggressive = aggressive;
	}
	
	/**
	 * Get the entity target
	 * 
	 * @return the target
	 */
	public Entity getTarget()
	{
		return target;
	}
	
	/**
	 * Should we be aggressive to the entity
	 * 
	 * @return true if the wraith is
	 */
	public Boolean isAggressive()
	{
		return aggressive;
	}
}

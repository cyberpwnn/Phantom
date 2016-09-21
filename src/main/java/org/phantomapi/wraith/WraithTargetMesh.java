package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.phantomapi.lang.GList;

/**
 * Represents a mesh of targets
 * 
 * @author cyberpwn
 */
public class WraithTargetMesh
{
	private GList<WraithTarget> targets;
	
	/**
	 * Create a target mesh
	 */
	public WraithTargetMesh()
	{
		this.targets = new GList<WraithTarget>();
	}
	
	/**
	 * Add a target
	 * 
	 * @param target
	 *            the target
	 */
	public void addTarget(WraithTarget target)
	{
		targets.add(target);
	}
	
	/**
	 * Add a target
	 * 
	 * @param target
	 *            the target
	 */
	public void addTarget(Location target)
	{
		addTarget(new WraithTarget(target));
	}
	
	/**
	 * Add a target
	 * 
	 * @param target
	 *            the target
	 */
	public void addTarget(Entity target)
	{
		addTarget(new WraithTarget(target));
	}
	
	/**
	 * Get the closest target
	 * 
	 * @param from
	 *            the location of the targeter
	 * @return a wraith target, or null. Throws invalid location checks for
	 *         different worlds
	 */
	public WraithTarget getClosestTarget(Location from)
	{
		if(hasTargets())
		{
			return null;
		}
		
		double distance = Double.MAX_VALUE;
		WraithTarget target = null;
		
		for(WraithTarget i : targets)
		{
			if(i.getTarget().distanceSquared(from) < distance)
			{
				distance = i.getTarget().distanceSquared(from);
				target = i;
			}
		}
		
		return target;
	}
	
	/**
	 * Does this mesh contain any targets
	 * 
	 * @return true if it does
	 */
	public boolean hasTargets()
	{
		return !targets.isEmpty();
	}
}

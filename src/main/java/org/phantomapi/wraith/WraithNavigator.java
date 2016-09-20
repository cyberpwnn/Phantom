package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Wraith navigation
 * 
 * @author cyberpwn
 */
public interface WraithNavigator
{
	/**
	 * Cancel navigation
	 */
	public void cancel();
	
	/**
	 * Get the target
	 * 
	 * @return the target
	 */
	public WraithTarget getTarget();
	
	/**
	 * Get the wraith
	 * 
	 * @return the wraith associated with this navigator
	 */
	public Wraith getWraith();
	
	/**
	 * Get the target type
	 * 
	 * @return the target type
	 */
	public WraithTargetType getTargetType();
	
	/**
	 * Is this wraith currently navigating
	 * 
	 * @return true if it is
	 */
	public boolean isNavigating();
	
	/**
	 * Is navigation paused?
	 * 
	 * @return true if it is
	 */
	public boolean isPaused();
	
	/**
	 * Set the navigation pause state
	 * 
	 * @param paused
	 *            the pause state
	 */
	public void setPaused(boolean paused);
	
	/**
	 * Set the target to an entity
	 * 
	 * @param entity
	 *            the entity
	 * @param aggressive
	 *            should the wraith be aggressive to the target?
	 */
	public void setTarget(Entity entity, boolean aggressive);
	
	/**
	 * Set the target to a location
	 * 
	 * @param target
	 *            the target
	 */
	public void setTarget(Location target);
}

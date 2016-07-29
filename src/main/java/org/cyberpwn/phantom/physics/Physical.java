package org.cyberpwn.phantom.physics;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Represents a physical object
 * 
 * @author cyberpwn
 *
 */
public interface Physical
{
	/**
	 * Get the location
	 * 
	 * @return the location
	 */
	public Location getLocation();
	
	/**
	 * Get the velocity
	 * 
	 * @return the vector
	 */
	public Vector getVelocity();
	
	/**
	 * Get the volume in the form of a vector w,h,d
	 * 
	 * @return the vector
	 */
	public Vector getVolume();
	
	/**
	 * Get the mass of the object
	 * 
	 * @return the double mass
	 */
	public Double getMass();
	
	/**
	 * Calculate density using the volume and mass
	 * 
	 * @return double density
	 */
	public Double getDensity();
	
	/**
	 * Influence gravity with another physical object
	 * 
	 * @param p
	 *            the other object
	 */
	public void influenceGravity(Physical p);
	
	/**
	 * Influence a force
	 * 
	 * @param v
	 *            the force
	 */
	public void influenceForce(Vector v);
}

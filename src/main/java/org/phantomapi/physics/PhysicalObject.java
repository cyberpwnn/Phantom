package org.phantomapi.physics;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Physical Object implementation
 * 
 * @author cyberpwn
 *
 */
public class PhysicalObject implements Physical
{
	protected Location location;
	protected Vector velocity;
	protected Vector volume;
	protected Double mass;
	
	/**
	 * Create a physical object unbound to an entity (movable)
	 * 
	 * @param location
	 *            the initial location
	 * @param volume
	 *            the volume
	 * @param mass
	 *            the mass
	 */
	public PhysicalObject(Location location, Vector volume, Double mass)
	{
		this.location = location;
		this.volume = volume;
		this.mass = mass;
		this.velocity = new Vector();
	}
	
	/**
	 * Create a physical object (movable)
	 * 
	 * @param location
	 *            the location
	 * @param volume
	 *            the volume
	 */
	public PhysicalObject(Location location, Vector volume)
	{
		this(location, volume, (volume.getX() * volume.getY() * volume.getZ()));
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public Vector getVelocity()
	{
		return velocity;
	}
	
	public Vector getVolume()
	{
		return volume;
	}
	
	public Double getMass()
	{
		return mass;
	}
	
	public Double getDensity()
	{
		return mass / (volume.getX() * volume.getY() * volume.getZ());
	}
	
	public void influenceGravity(Physical p)
	{
		p.influenceGravity(this);
		Double distance = p.getLocation().distance(getLocation());
		Double force = ((mass * p.getMass()) / distance);
		Vector velocity = p.getLocation().subtract(location).toVector().normalize().multiply(force);
		influenceForce(velocity);
	}
	
	@Override
	public void influenceForce(Vector v)
	{
		velocity.add(v);
		location.add(velocity);
	}
}

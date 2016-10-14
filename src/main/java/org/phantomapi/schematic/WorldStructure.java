package org.phantomapi.schematic;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.Cuboid;

/**
 * Structure of world artifacts which could internest itself
 * 
 * @author cyberpwn
 *
 */
public class WorldStructure implements Artifact
{
	private GMap<Vector, Artifact> artifacts;
	private Location location;
	private Cuboid cuboid;
	private Boolean built;
	
	/**
	 * The world structure creation
	 * 
	 * @param location
	 *            the location
	 */
	public WorldStructure(Location location)
	{
		this.location = location;
		this.cuboid = new Cuboid(location);
		this.built = false;
		this.artifacts = new GMap<Vector, Artifact>();
	}
	
	/**
	 * Add an artifact to this artifact structure, vector determining relative
	 * position from the super-position
	 * 
	 * @param a
	 *            the artifact
	 * @param v
	 *            the vector
	 */
	public void add(Artifact a, Vector v)
	{
		artifacts.put(v, a);
	}
	
	@Override
	public void build()
	{
		for(Artifact i : artifacts.v())
		{
			i.build();
		}
		
		built = true;
	}
	
	@Override
	public void move(Location to)
	{
		Vector v = to.subtract(location).toVector();
		move(v);
	}
	
	@Override
	public void move(Vector vec)
	{
		for(Artifact i : artifacts.v())
		{
			i.move(vec);
		}
	}
	
	@Override
	public void clear()
	{
		for(Artifact i : artifacts.v())
		{
			i.clear();
		}
		
		built = false;
	}
	
	@Override
	public Location getLocation()
	{
		return location;
	}
	
	@Override
	public Location getCenter()
	{
		return location;
	}
	
	@Override
	public Cuboid toCuboid()
	{
		return cuboid;
	}
	
	@Override
	public Boolean isBuilt()
	{
		return built;
	}
	
}

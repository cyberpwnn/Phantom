package org.cyberpwn.phantom.world;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.cyberpwn.phantom.lang.GMap;

public class WorldStructure implements Artifact
{
	private GMap<Vector, Artifact> artifacts;
	private Location location;
	private Cuboid cuboid;
	private Boolean built;
	
	public WorldStructure(Location location)
	{
		this.location = location;
		this.cuboid = new Cuboid(location);
		this.built = false;
		this.artifacts = new GMap<Vector, Artifact>();
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

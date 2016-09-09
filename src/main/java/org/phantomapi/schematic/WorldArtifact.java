package org.phantomapi.schematic;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.phantomapi.world.Cuboid;

/**
 * World artifact 
 * 
 * @author cyberpwn
 *
 */
public class WorldArtifact implements Artifact
{
	private Location location;
	private Location center;
	private Cuboid cuboid;
	private Schematic schematic;
	private Schematic ghost;
	private Boolean built;
	
	/**
	 * Make a world artifact at a location
	 * @param location the location
	 * @param schematic the schematics
	 */
	public WorldArtifact(Location location, Schematic schematic)
	{
		this.location = location;
		this.schematic = schematic;
		this.center = location.clone().add(new Vector(schematic.getDimension().getWidth() / 2, schematic.getDimension().getHeight() / 2, schematic.getDimension().getDepth() / 2));
		this.cuboid = new Cuboid(location, location.clone().add(new Vector(schematic.getDimension().getWidth() - 1, schematic.getDimension().getHeight() - 1, schematic.getDimension().getDepth() - 1)));
		this.ghost = new Schematic(schematic.getDimension());
		this.built = false;
	}
	
	@Override
	public void build()
	{
		if(!built)
		{
			ghost.read(getLocation());
		}
		
		schematic.apply(getLocation());
		built = true;
	}
	
	@Override
	public Location getLocation()
	{
		return location;
	}
	
	@Override
	public Location getCenter()
	{
		return center;
	}
	
	@Override
	public Cuboid toCuboid()
	{
		return cuboid;
	}
	
	@Override
	public void move(Location to)
	{
		clear();
		location = to;
		center = location.clone().add(new Vector(schematic.getDimension().getWidth() / 2, schematic.getDimension().getHeight() / 2, schematic.getDimension().getDepth() / 2));
		cuboid = new Cuboid(location, location.clone().add(new Vector(schematic.getDimension().getWidth() - 1, schematic.getDimension().getHeight() - 1, schematic.getDimension().getDepth() - 1)));
		ghost = new Schematic(schematic.getDimension());
		build();
	}
	
	@Override
	public void move(Vector vec)
	{
		clear();
		location = location.clone().add(vec);
		center = location.clone().add(new Vector(schematic.getDimension().getWidth() / 2, schematic.getDimension().getHeight() / 2, schematic.getDimension().getDepth() / 2));
		cuboid = new Cuboid(location, location.clone().add(new Vector(schematic.getDimension().getWidth() - 1, schematic.getDimension().getHeight() - 1, schematic.getDimension().getDepth() - 1)));
		ghost = new Schematic(schematic.getDimension());
		build();
	}
	
	@Override
	public void clear()
	{
		if(built)
		{
			ghost.apply(location);
			built = false;
		}
	}
	
	@Override
	public Boolean isBuilt()
	{
		return built;
	}
}

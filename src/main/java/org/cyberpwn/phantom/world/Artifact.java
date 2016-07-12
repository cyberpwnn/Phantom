package org.cyberpwn.phantom.world;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface Artifact
{
	public void build();
	public void move(Location location);
	public void move(Vector vec);
	public void clear();
	public Location getLocation();
	public Location getCenter();
	public Cuboid toCuboid();
	public Boolean isBuilt();
}

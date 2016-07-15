package org.cyberpwn.phantom.game;

import org.bukkit.Location;
import org.bukkit.World;

public interface GameMap<M, G, T, P>
{
	public boolean contains(Location location);
	public World getWorld();
}

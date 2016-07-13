package org.cyberpwn.phantom.game;

import org.bukkit.Location;

public interface GameMap<M, G, T, P>
{
	public boolean contains(Location location);
}

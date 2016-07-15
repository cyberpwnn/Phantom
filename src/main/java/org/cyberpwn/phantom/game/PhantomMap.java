package org.cyberpwn.phantom.game;

import org.bukkit.Location;
import org.bukkit.World;

public class PhantomMap<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> implements GameMap<M, G, T, P>
{
	protected World world;
	
	public PhantomMap(World world)
	{
		this.world = world;
	}
	
	@Override
	public boolean contains(Location location)
	{
		return false;
	}

	@Override
	public World getWorld()
	{
		return world;
	}
}

package org.cyberpwn.phantom.game;

import org.bukkit.World;

public class ExampleMap extends PhantomChunkMap<ExampleMap, ExampleGame, ExampleTeam, ExamplePlayer>
{
	//This type of map stores chunks for regions
	//Any map data, objects, and everything else goes here
	public ExampleMap(World world)
	{
		super(world);
	}
}

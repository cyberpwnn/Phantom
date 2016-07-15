package org.cyberpwn.phantom.game;

import org.bukkit.World;
import org.cyberpwn.phantom.lang.GList;

public class PhantomRegionedMap<R extends Region<R, M, G, T, P>, M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> extends PhantomMap<M, G, T, P> implements RegionedMap<R, M, G, T, P>
{
	protected GList<R> regions;
	
	public PhantomRegionedMap(World world)
	{
		super(world);
		
		this.regions = new GList<R>();
	}
	
	@Override
	public GList<R> getRegions()
	{
		return regions;
	}

	@Override
	public void addRegion(R r)
	{
		regions.add(r);
	}

	@Override
	public boolean contains(R r)
	{
		return regions.contains(r);
	}
	
}

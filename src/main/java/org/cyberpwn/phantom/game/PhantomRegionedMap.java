package org.cyberpwn.phantom.game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;

/**
 * Regioned map implementation. This is a map, and can contain regions aswell.
 * 
 * @author cyberpwn
 *
 */
public class PhantomRegionedMap extends PhantomMap implements RegionedMap
{
	private GList<Region> regions;
	
	public PhantomRegionedMap(World world)
	{
		super(world);
		
		this.regions = new GList<Region>();
	}
	
	@Override
	public GList<Region> getRegions()
	{
		return regions;
	}
	
	@Override
	public void add(Region r)
	{
		regions.add(r);
	}
	
	@Override
	public Region getRegion(Location l)
	{
		for(Region i : regions)
		{
			if(i.contains(l))
			{
				return i;
			}
		}
		
		return null;
	}
	
	@Override
	public Region getRegion(Entity e)
	{
		return getRegion(e);
	}
	
	@Override
	public Region getRegion(Player p)
	{
		return getRegion(p);
	}
	
	@Override
	public Region getRegion(GamePlayer p)
	{
		return getRegion(p);
	}
}

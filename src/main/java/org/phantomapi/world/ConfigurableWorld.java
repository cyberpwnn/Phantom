package org.phantomapi.world;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;

public class ConfigurableWorld extends ConfigurableObject
{
	@Comment("Removes tnt and sand entities above the world height limit")
	@Keyed("entities.tnt-height-nerf")
	public boolean keepInWorld = false;
	
	@Comment("Prevents tnt from being pushed by water flow")
	@Keyed("entities.tnt-ignores-water-flow")
	public boolean ignoreWaterFlow = false;
	
	@Comment("Loads chunks in the direction tnt is moving")
	@Keyed("entities.tnt-load-chunks")
	public boolean loadNearbyChunks = false;
	
	@Comment("Spawn tnt perfectly in the block they were ignited from")
	@Keyed("entities.tnt-perfect-spawn")
	public boolean tntPerfectSpawn = false;
	
	@Comment("Limit redstone in a certain way")
	@Keyed("limits.limit-redstone")
	public boolean limitRedstone = false;
	
	@Comment("Define the routine section to limit.\nValues include;\n CHUNK\n ISLAND")
	@Keyed("limits.redstone.routine")
	public String limitRedstoneMethod = "CHUNK";
	
	@Comment("Define the interval (how much redstone per tick to allow) per routine")
	@Keyed("limits.redstone.interval")
	public int limitRedstoneInterval = 36;
	
	@Comment("The bleed out speed (2.0 is twice as fast as normal)")
	@Keyed("limits.redstone.bleed")
	public double redstoneBleed = 2.0;
	
	private World world;
	private GList<Entity> entityMapping;
	private GMap<Chunk, Integer> cache;
	
	public ConfigurableWorld(World world)
	{
		super(world.getName());
		
		cache = new GMap<Chunk, Integer>();
		entityMapping = new GList<Entity>();
		this.world = world;
	}
	
	public boolean updateRedstone(Location l)
	{
		if(limitRedstone && l.getWorld().equals(world))
		{
			if(!cache.contains(l.getChunk()))
			{
				cache.put(l.getChunk(), 0);
			}
			
			if(cache.get(l.getChunk()) > limitRedstoneInterval)
			{
				return false;
			}
			
			cache.put(l.getChunk(), cache.get(l.getChunk()) + 1);
		}
		
		return true;
	}
	
	public void update()
	{
		entityMapping.clear();
		cache.clear();
		
		for(Entity i : world.getEntities())
		{
			if(isKeepInWorld() && (i instanceof FallingBlock || i instanceof TNTPrimed))
			{
				if(i.getLocation().getY() > world.getMaxHeight())
				{
					i.remove();
					continue;
				}
				
				if(i instanceof TNTPrimed && tntPerfectSpawn)
				{
					if(i.getTicksLived() < 2)
					{
						i.teleport(i.getLocation().clone().getBlock().getLocation().clone().add(0.5, 0.5, 0.5));
						i.setVelocity(new Vector(0, 0, 0));
					}
				}
				
				if(i instanceof TNTPrimed && ignoreWaterFlow)
				{
					Location l = i.getLocation();
					
					if(l.getBlock().isLiquid())
					{
						i.setVelocity(new Vector(0, i.getVelocity().getY(), 0));
					}
				}
				
				if(i instanceof TNTPrimed && loadNearbyChunks)
				{
					Location l = i.getLocation();
					Vector v = i.getVelocity();
					Location t = l.clone().add(v);
					Chunk c = t.getChunk();
					
					for(Chunk j : W.chunkFaces(c))
					{
						j.load();
					}
				}
			}
			
			entityMapping.add(i);
		}
	}
	
	public boolean isKeepInWorld()
	{
		return keepInWorld;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public GList<Entity> getEntityMapping()
	{
		return entityMapping;
	}
}

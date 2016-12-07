package org.phantomapi.world;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.lang.GMap;

public class ConfigurableWorld extends ConfigurableObject
{
	@Keyed("entities.keep-in-world")
	public boolean keepInWorld = false;
	
	private World world;
	private GMap<Entity, Integer> entityMapping;
	
	public ConfigurableWorld(World world)
	{
		super(world.getName());
		
		entityMapping = new GMap<Entity, Integer>();
		this.world = world;
	}
	
	public void update()
	{
		entityMapping.clear();
		
		for(Entity i : world.getEntities())
		{
			if(isKeepInWorld() && !(i instanceof Player) && !(i instanceof Item))
			{
				if(i.getLocation().getY() > world.getMaxHeight())
				{
					i.remove();
					continue;
				}
			}
			
			entityMapping.put(i, i.getEntityId());
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
	
	public GMap<Entity, Integer> getEntityMapping()
	{
		return entityMapping;
	}
}

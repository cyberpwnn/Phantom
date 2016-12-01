package org.phantomapi.spawner;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class PhantomSpawner
{
	private Block block;
	
	public PhantomSpawner(Block block)
	{
		this.block = block;
	}
	
	public void setType(EntityType type)
	{
		CreatureSpawner c = (CreatureSpawner) block.getState();
		c.setSpawnedType(type);
		
		try
		{
			Class<?> ct = Class.forName("org.bukkit.entity.CreatureType");
			Object ctype = ct.getMethod("fromEntityType", EntityType.class).invoke(null, type);
			c.getClass().getMethod("setCreatureType", ct).invoke(c, ctype);
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	public EntityType getType()
	{
		CreatureSpawner c = (CreatureSpawner) block.getState();
		
		return c.getSpawnedType();
	}
}

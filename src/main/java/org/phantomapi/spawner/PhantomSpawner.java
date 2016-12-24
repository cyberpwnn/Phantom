package org.phantomapi.spawner;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

/**
 * Phantom spawner setting
 * 
 * @author cyberpwn
 */
public class PhantomSpawner
{
	private Block block;
	
	public PhantomSpawner(Block block)
	{
		this.block = block;
	}
	
	/**
	 * Set the type of the spawner
	 * 
	 * @param type
	 *            the entity type
	 */
	public void setType(EntityType type)
	{
		try
		{
			CreatureSpawner c = (CreatureSpawner) block.getState();
			c.setSpawnedType(type);
			Class<?> ct = Class.forName("org.bukkit.entity.CreatureType");
			Object ctype = ct.getMethod("fromEntityType", EntityType.class).invoke(null, type);
			c.getClass().getMethod("setCreatureType", ct).invoke(c, ctype);
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	/**
	 * Get the type of the spawner
	 * 
	 * @return the entity type or null
	 */
	public EntityType getType()
	{
		try
		{
			CreatureSpawner c = (CreatureSpawner) block.getState();
			
			return c.getSpawnedType();
		}
		
		catch(Exception e)
		{
			return null;
		}
	}
}

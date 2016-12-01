package org.phantomapi.core;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.nest.Nest;
import org.phantomapi.spawner.PhantomSpawner;

public class SpawnerController extends Controller
{
	public SpawnerController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public void on(BlockPlaceEvent e)
	{
		Nest.getBlock(e.getBlock()).remove("s.t");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(SpawnerSpawnEvent e)
	{
		Block b = e.getSpawner().getBlock();
		PhantomSpawner s = new PhantomSpawner(b);
		
		if(!Nest.getBlock(b).contains("s.t"))
		{
			Nest.getBlock(b).set("s.t", (int) s.getType().getTypeId());
		}
		
		if(s.getType().equals(EntityType.PIG))
		{
			if(Nest.getBlock(b).getInt("s.t") != (int) s.getType().getTypeId())
			{
				s.setType(EntityType.fromId(Nest.getBlock(b).getInt("s.t")));
				e.getLocation().getWorld().spawnEntity(e.getLocation(), s.getType());
				b.getState().update();
				e.setCancelled(true);
			}
		}
		
		else if(Nest.getBlock(b).getInt("s.t") != (int) s.getType().getTypeId())
		{
			Nest.getBlock(b).set("s.t", (int) s.getType().getTypeId());
		}
	}
}

package org.phantomapi.core;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.ConfigurableWorld;

@Ticked(0)
public class WorldController extends Controller
{
	private GMap<World, ConfigurableWorld> worlds;
	
	public WorldController(Controllable parentController)
	{
		super(parentController);
		
		worlds = new GMap<World, ConfigurableWorld>();
	}
	
	public void add(World world)
	{
		ConfigurableWorld cw = new ConfigurableWorld(world);
		loadCluster(cw, "worlds");
		worlds.put(world, cw);
	}
	
	public void remove(World world)
	{
		worlds.remove(world);
	}
	
	@EventHandler
	public void on(WorldUnloadEvent e)
	{
		remove(e.getWorld());
	}
	
	@EventHandler
	public void on(WorldLoadEvent e)
	{
		add(e.getWorld());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(BlockRedstoneEvent e)
	{
		if(worlds.k().contains(e.getBlock().getWorld()))
		{
			if(!get(e.getBlock().getWorld()).updateRedstone(e.getBlock().getLocation()))
			{
				e.setNewCurrent((int) (e.getNewCurrent() / get(e.getBlock().getWorld()).redstoneBleed));
			}
		}
	}
	
	@Override
	public void onStart()
	{
		for(World i : Bukkit.getWorlds())
		{
			add(i);
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public void onTick()
	{
		for(World i : worlds.k())
		{
			worlds.get(i).update();
		}
	}
	
	public ConfigurableWorld get(World world)
	{
		return worlds.get(world);
	}
}

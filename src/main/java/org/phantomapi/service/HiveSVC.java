package org.phantomapi.service;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import phantom.hive.HiveWorld;
import phantom.lang.GMap;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Hive service for storing data on blocks, entities, chunks, and worlds
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Hive")
@Singular
public class HiveSVC implements IService
{
	private GMap<World, HiveWorld> worlds;

	@Start
	public void start()
	{
		for(World i : Bukkit.getWorlds())
		{
			installWorld(i);
		}
	}

	@Stop
	public void stop()
	{
		for(World i : Bukkit.getWorlds())
		{
			uninstallWorld(i);
		}
	}

	public HiveWorld getWorld(World world)
	{
		return worlds.get(world);
	}

	private void installWorld(World world)
	{
		worlds.put(world, new HiveWorld(world));
	}

	private void uninstallWorld(World world)
	{
		worlds.remove(world);
	}

	@EventHandler
	public void on(WorldLoadEvent e)
	{
		installWorld(e.getWorld());
	}

	@EventHandler
	public void on(WorldUnloadEvent e)
	{
		installWorld(e.getWorld());
	}
}

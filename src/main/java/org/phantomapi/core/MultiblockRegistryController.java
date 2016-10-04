package org.phantomapi.core;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GMap;
import org.phantomapi.multiblock.Multiblock;
import org.phantomapi.multiblock.MultiblockStructure;
import org.phantomapi.sync.TaskLater;

/**
 * Manages multiblock registries
 * 
 * @author cyberpwn
 */
public class MultiblockRegistryController extends Controller
{
	private GMap<String, MultiblockStructure> structures;
	private GMap<Integer, Multiblock> instances;
	
	public MultiblockRegistryController(Controllable parentController)
	{
		super(parentController);
		
		this.structures = new GMap<String, MultiblockStructure>();
		this.instances = new GMap<Integer, Multiblock>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public void registerStructure(MultiblockStructure mb)
	{
		structures.put(mb.getType(), mb);
	}
	
	public void unRegisterStructure(MultiblockStructure mb)
	{
		structures.remove(mb.getType());
	}
	
	@EventHandler
	public void on(BlockPlaceEvent e)
	{
		new TaskLater()
		{
			@Override
			public void run()
			{
				for(String i : structures.k())
				{
					GMap<Vector, Location> map = structures.get(i).match(e.getBlock().getLocation());
					
					if(map != null)
					{
						
						break;
					}
				}
			}
		};
	}
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		
	}
}

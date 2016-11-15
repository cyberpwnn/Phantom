package org.phantomapi.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.statistics.Monitorable;

@Ticked(5)
public class BlockMetaController extends Controller implements Monitorable
{
	public BlockMetaController(Controllable parentController)
	{
		super(parentController);
	}
	
	@EventHandler
	public void on(ChunkLoadEvent e)
	{
		
	}
	
	@EventHandler
	public void on(ChunkUnloadEvent e)
	{
		
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
	}
	
	@Override
	public void onTick()
	{
		
	}
	
	@Override
	public String getMonitorableData()
	{
		return "";
	}
}

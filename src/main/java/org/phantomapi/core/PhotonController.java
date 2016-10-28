package org.phantomapi.core;

import org.bukkit.Chunk;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSet;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;
import org.phantomapi.world.L;

/**
 * Relighting
 * 
 * @author cyberpwn
 */
@Ticked(20)
public class PhotonController extends Controller implements Monitorable
{
	private GSet<Chunk> queue;
	private Boolean wait;
	
	public PhotonController(Controllable parentController)
	{
		super(parentController);
		
		queue = new GSet<Chunk>();
		wait = false;
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
		if(wait)
		{
			wait = false;
			return;
		}
		
		if(!queue.isEmpty())
		{
			for(Chunk i : new GList<Chunk>(queue))
			{
				L.relight(i);
			}
			
			queue.clear();
		}
	}
	
	public void relight(Chunk chunk)
	{
		if(queue.contains(chunk))
		{
			return;
		}
		
		if(queue.size() < 344)
		{
			wait = true;
		}
		
		queue.add(chunk);
	}
	
	@Override
	public String getMonitorableData()
	{
		return "Photons: " + C.LIGHT_PURPLE + queue.size() + " " + (wait ? C.RED + "> WAITING" : (queue.isEmpty() ? "" : C.AQUA + "> RUNNING"));
	}
}

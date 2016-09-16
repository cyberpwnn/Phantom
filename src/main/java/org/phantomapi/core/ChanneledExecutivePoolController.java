package org.phantomapi.core;

import org.phantomapi.clust.DataCluster;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.ExecutiveIterator;
import org.phantomapi.sync.ExecutivePool;
import org.phantomapi.util.Average;
import org.phantomapi.util.C;
import org.phantomapi.util.D;
import org.phantomapi.util.F;

/**
 * The Channeled Executive Pool Controller pools ExeutiveIterator<T> objects
 * through channels. No need to run your own tasks for them.
 * 
 * @author cyberpwn
 *
 */
@Ticked(0)
public class ChanneledExecutivePoolController extends Controller implements Monitorable
{
	private GMap<String, ExecutivePool> pools;
	public static int hit = 0;
	private Average ag;
	
	public ChanneledExecutivePoolController(Controllable parentController)
	{
		super(parentController);
		
		hit = 0;
		ag = new Average(8);
		pools = new GMap<String, ExecutivePool>();
	}
	
	public void onTick()
	{
		D.flush();
		
		if(!pools.isEmpty())
		{
			for(String i : pools.k())
			{
				if(pools.get(i).isIdle())
				{
					pools.get(i).cancel();
					pools.remove(i);
					w("Closed Execution Stream: " + C.RED + i);
				}
			}
		}
		
		hit = 0;
		
		ag.put(DataCluster.perm);
		DataCluster.perm = 0;
		DataCluster.permX = (long) ag.getAverage();
	}
	
	public void fire(String channel, ExecutiveIterator<?> it)
	{
		if(!pools.containsKey(channel))
		{
			s("Opened Execution Stream: " + C.AQUA + channel);
			pools.put(channel, new ExecutivePool((double) 1, 0));
		}
		
		pools.get(channel).add(it);
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
	public String getMonitorableData()
	{
		int siz = 0;
		
		for(String i : pools.k())
		{
			siz += pools.get(i).size();
		}
		
		return "Pools: " + C.LIGHT_PURPLE + pools.size() + C.DARK_GRAY + " Total: " + C.LIGHT_PURPLE + F.f(siz) + C.DARK_GRAY + " Activity: " + C.LIGHT_PURPLE + F.f(hit);
	}
}

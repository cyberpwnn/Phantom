package org.phantomapi.core;

import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.ExecutiveIterator;
import org.phantomapi.sync.ExecutivePool;
import org.phantomapi.util.C;
import org.phantomapi.util.D;

/**
 * The Channeled Executive Pool Controller pools ExeutiveIterator<T> objects
 * through channels. No need to run your own tasks for them.
 * 
 * @author cyberpwn
 *
 */
@Ticked(0)
public class ChanneledExecutivePoolController extends Controller
{
	private GMap<String, ExecutivePool> pools;
	
	public ChanneledExecutivePoolController(Controllable parentController)
	{
		super(parentController);
		
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
}

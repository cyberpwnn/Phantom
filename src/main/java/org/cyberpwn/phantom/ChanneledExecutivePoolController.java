package org.cyberpwn.phantom;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.construct.Ticked;
import org.cyberpwn.phantom.lang.GMap;
import org.cyberpwn.phantom.sync.ExecutiveIterator;
import org.cyberpwn.phantom.sync.ExecutivePool;
import org.cyberpwn.phantom.util.C;
import org.cyberpwn.phantom.util.F;

@Ticked(20)
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
	
	public void fire(String channel, ExecutiveIterator<?> it)
	{
		if(!pools.containsKey(channel))
		{
			s("Opened Execution Stream: " + C.AQUA + channel);
			pools.put(channel, new ExecutivePool((double) 1, 0));
		}
		
		s("Funneled " + C.LIGHT_PURPLE + F.f(it.size()) + C.GREEN + " opterations into " + C.AQUA + channel + C.LIGHT_PURPLE + " " + F.f(pools.get(channel).size()) + C.GREEN + " total.");
	}
}

package org.phantomapi.core;

import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

@Ticked(0)
public class HyveController extends ConfigurableController implements Monitorable
{
	@Comment("The maximum amount of time to spend ticking blocks")
	@Keyed("max-tick-ms")
	public double maxTickMs = 2.5;
	
	private int vtick = 0;
	
	public HyveController(Controllable parentController)
	{
		super(parentController, "hyve");
		
		loadCluster(this);
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
		return "V: " + C.LIGHT_PURPLE + F.f(vtick);
	}
}

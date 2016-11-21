package org.phantomapi.core;

import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import redis.clients.jedis.Jedis;

@Ticked(0)
public class PPAController extends ConfigurableController
{
	@Keyed("identifier")
	@Comment("The identifier for this server.\nUse a human readable name such as the server name.")
	public String id = "null";
	
	private Boolean running;
	private Jedis r;
	
	public PPAController(Controllable parentController)
	{
		super(parentController, "ppa");
		
		running = false;
	}
	
	@Override
	public void onStart()
	{
		loadCluster(this);
		
		r = Phantom.instance().getRedisConnectionController().createSplitConnection();
	}
	
	@Override
	public void onTick()
	{
		if(!running)
		{
			running = true;
			
			new A()
			{
				@Override
				public void async()
				{
					
				}
			};
		}
	}
	
	@Override
	public void onStop()
	{
		r.disconnect();
	}
}

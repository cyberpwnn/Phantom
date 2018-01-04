package org.phantomapi;

import org.phantomapi.core.PhantomProvider;

import phantom.dispatch.PD;
import phantom.sched.TICK;
import phantom.sched.Task;

public class DMSP
{
	private Task task;
	private PhantomProvider api;

	public DMSP()
	{
		PD.v("DMSp Initialized");
		api = new PhantomProvider();
	}

	public void start()
	{
		startTickMethod();
		Phantom.activate(api);
		PD.v("DMSp Online");
	}

	public void stop()
	{
		task.cancel();
		Phantom.deactivate(api);
		PD.v("DMSp Offline");
	}

	private void tick()
	{
		TICK.tick++;
		Phantom.getPawnSpace().tick();
	}

	private void startTickMethod()
	{
		task = new Task(0)
		{
			@Override
			public void run()
			{
				tick();
			}
		};
	}

	public PhantomProvider getApi()
	{
		return api;
	}
}

package org.phantomapi;

import org.phantomapi.core.PhantomController;
import phantom.dispatch.PD;
import phantom.scheduler.TICK;
import phantom.scheduler.Task;

public class DMSP
{
	private Task task;
	private PhantomController api;

	public DMSP()
	{
		PD.v("DMSp Initialized");
		api = new PhantomController();
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
}

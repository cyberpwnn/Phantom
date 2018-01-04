package org.phantomapi;

import org.phantomapi.core.PhantomProvider;

import phantom.dispatch.PD;
import phantom.sched.TICK;
import phantom.sched.Task;
import phantom.util.metrics.Documented;

/**
 * DMSP Used for managing the phantom api
 *
 * @author cyberpwn
 */
@Documented
public class DMSP
{
	private Task task;
	private PhantomProvider api;

	/**
	 * Create a dmsp instance
	 */
	public DMSP()
	{
		PD.v("DMSp Initialized");
		api = new PhantomProvider();
	}

	/**
	 * Start DMSP
	 */
	public void start()
	{
		startTickMethod();
		Phantom.activate(api);
		PD.v("DMSp Online");
	}

	/**
	 * Stop DMSP
	 */
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

	/**
	 * Get the root phantom provider
	 *
	 * @return the provider
	 */
	public PhantomProvider getApi()
	{
		return api;
	}
}

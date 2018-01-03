package org.phantomapi.core;

import org.phantomapi.Phantom;
import phantom.dispatch.PD;
import phantom.scheduler.S;
import phantom.scheduler.TICK;
import phantom.scheduler.Task;

public class DMS
{
	private Task task;
	
	public DMS()
	{
		PD.v("DMS Initialized");
	}
	
	public void start()
	{
		startTickMethod();
		PD.v("DMS Online");
		SomePawn pawn = new SomePawn();
		Phantom.activate(pawn);
		
		new S(20)
		{
			@Override
			public void run()
			{
				Phantom.deactivate(pawn);
			}
		};
	}
	
	public void stop()
	{
		task.cancel();
		PD.v("DMS Offline");
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

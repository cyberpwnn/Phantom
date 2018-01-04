package org.phantomapi.service;

import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.sched.A;
import phantom.sched.ParallelPoolManager;
import phantom.sched.QueueMode;
import phantom.service.IService;

@Name("SVC Thread Pool")
@Singular
public class ThreadPoolService implements IService
{
	private ParallelPoolManager pool;

	@Start
	public void start()
	{
		pool = new ParallelPoolManager("Phantom Ghost", 2, QueueMode.ROUND_ROBIN)
		{
			@Override
			public long getNanoGate()
			{
				return 1000000;
			}
		};
	}

	@Stop
	public void stop()
	{
		pool.shutdown();
	}

	public void run(A a)
	{
		pool.queue(a);
	}
}

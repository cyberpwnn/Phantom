package com.volmit.phantom.plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RejectedExecutionException;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.time.M;

public class TaskManager
{
	private GList<Runnable> sync;
	private GList<Runnable> async;
	private ExecutorService exe;
	
	public TaskManager()
	{
		final ForkJoinWorkerThreadFactory factory = new ForkJoinWorkerThreadFactory()
		{
			@Override
			public ForkJoinWorkerThread newThread(ForkJoinPool pool)
			{
				final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
				worker.setName("Phantom Executor " + (worker.getPoolIndex() + 1));
				worker.setPriority(Thread.MIN_PRIORITY);
				return worker;
			}
		};

		exe = new ForkJoinPool(16, factory, null, false);
		sync = new GList<Runnable>();
		async = new GList<Runnable>();
	}
	
	public void tick()
	{
		for(Runnable r : async)
		{
			try
			{
				async.remove(r);
				exe.submit(r);
			}
			
			catch(RejectedExecutionException e)
			{
				async.add(r);
			}
			
			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
		
		long start = M.ns();
		long alloc = (long) (getAllocatedTime() * 1000000000D);
		
		while(M.ns() - start < alloc && !sync.isEmpty())
		{
			try
			{
				sync.pop().run();
			}
			
			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private double getAllocatedTime()
	{
		return 5;
	}

	public void sync(Runnable r)
	{
		sync.add(r);
	}
	
	public void async(Runnable r)
	{
		async.add(r);
	}
}

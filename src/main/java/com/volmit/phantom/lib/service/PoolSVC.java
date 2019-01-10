package com.volmit.phantom.lib.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RejectedExecutionException;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.util.plugin.Alphabet;

public class PoolSVC extends Thread implements IService, Runnable
{
	private int idx;
	private GMap<Integer, Integer> intervals;
	private GMap<Integer, Runnable> repeats;
	private GMap<Runnable, Integer> delayTasks;
	private GList<Runnable> tasks;
	private GList<Runnable> nextTasks;
	private ExecutorService executorService;

	@Override
	public void onStart()
	{
		setPriority(MIN_PRIORITY);
		idx = Integer.MIN_VALUE;
		tasks = new GList<>();
		nextTasks = new GList<>();
		intervals = new GMap<>();
		repeats = new GMap<>();
		delayTasks = new GMap<>();
		executorService = new ForkJoinPool(16, new ForkJoinWorkerThreadFactory()
		{
			@Override
			public ForkJoinWorkerThread newThread(ForkJoinPool pool)
			{
				final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
				worker.setName("Phantom Worker " + Alphabet.values()[worker.getPoolIndex()]);
				worker.setPriority(Thread.MIN_PRIORITY);
				return worker;
			}
		}, new UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				e.printStackTrace();
			}
		}, true);
		start();
	}

	@Override
	public void onStop()
	{
		this.interrupt();
	}

	@Override
	public void run()
	{
		while(!interrupted())
		{
			try
			{
				Thread.sleep(50);
			}

			catch(InterruptedException e)
			{
				e.printStackTrace();
				break;
			}

			try
			{
				M.uptickAsync();

				for(Runnable i : delayTasks.k())
				{
					if(M.tickAsync() >= delayTasks.get(i))
					{
						delayTasks.remove(i);
						tasks.add(i);
					}
				}

				for(Integer i : repeats.k())
				{
					if(M.intervalAsync(intervals.get(i)))
					{
						tasks.add(repeats.get(i));
					}
				}

				while(!tasks.isEmpty())
				{
					try
					{
						Runnable r = tasks.pop();

						try
						{
							executorService.submit(r);
						}

						catch(RejectedExecutionException e)
						{
							// Pfft
							tasks.addFirst(r);
							break;
						}
					}

					catch(Throwable e)
					{
						e.printStackTrace();
					}
				}

				tasks.addAll(nextTasks);
				nextTasks.clear();
			}

			catch(Throwable e)
			{

			}
		}
	}

	public void queue(Runnable r)
	{
		nextTasks.add(r);
	}

	public void dequeueRepeating(int id)
	{
		intervals.remove(id);
		repeats.remove(id);
	}

	public int queueRepeating(Runnable r, int interval)
	{
		int id = ++idx;

		queue(new Runnable()
		{
			@Override
			public void run()
			{
				intervals.put(id, interval);
				repeats.put(id, r);
			}
		});

		return id;
	}

	public void queueDelayed(Runnable r, int delay)
	{
		queue(new Runnable()
		{
			@Override
			public void run()
			{
				delayTasks.put(r, delay);
			}
		});
	}
}

package com.volmit.phantom.lib.service;

import org.bukkit.Bukkit;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.main.PhantomPlugin;
import com.volmit.phantom.util.queue.PhantomQueue;
import com.volmit.phantom.util.queue.QueueExecutor;
import com.volmit.phantom.util.queue.ThrottledExecutor;

public class TaskSVC implements IService, Runnable
{
	private int idx;
	private GMap<Integer, Integer> intervals;
	private GMap<Integer, Runnable> repeats;
	private GMap<Runnable, Long> delayTasks;
	private GList<Runnable> tasks;
	private GList<Runnable> nextTasks;
	private QueueExecutor<Runnable> ex;

	@Override
	public void onStart()
	{
		idx = Integer.MIN_VALUE;
		tasks = new GList<>();
		nextTasks = new GList<>();
		intervals = new GMap<>();
		repeats = new GMap<>();
		delayTasks = new GMap<>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(PhantomPlugin.plugin, this, 0, 0);

		ex = new ThrottledExecutor<Runnable>()
		{
			@Override
			public void execute(Runnable t)
			{
				try
				{
					t.run();
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		};
		ex.async(false);
		ex.interval(0);
		ex.queue(new PhantomQueue<Runnable>());
		ex.start();
	}

	@Override
	public void onStop()
	{

	}

	@Override
	public void run()
	{
		try
		{
			M.uptick();

			for(Runnable i : delayTasks.k())
			{
				if(M.tick() >= delayTasks.get(i))
				{
					delayTasks.remove(i);
					tasks.add(i);
				}
			}

			for(Integer i : repeats.k())
			{
				if(M.interval(intervals.get(i)))
				{
					tasks.add(repeats.get(i));
				}
			}

			while(!tasks.isEmpty())
			{
				try
				{
					tasks.pop().run();
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

	public void queueLazy(Runnable r)
	{
		ex.getQueue().queue(r);
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
				delayTasks.put(r, M.tick() + delay);
			}
		});
	}
}

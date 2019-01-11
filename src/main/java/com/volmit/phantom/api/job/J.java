package com.volmit.phantom.api.job;

import com.volmit.phantom.api.lang.FinalInteger;
import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.api.sheduler.AR;
import com.volmit.phantom.api.sheduler.SR;
import com.volmit.phantom.lib.service.PoolSVC;
import com.volmit.phantom.lib.service.TaskSVC;
import com.volmit.phantom.util.plugin.Documented;

/**
 * Job Scheduling
 *
 * @author cyberpwn
 *
 */
@Documented
public class J
{
	private static GList<Runnable> afterStartup = new GList<>();
	private static GList<Runnable> afterStartupAsync = new GList<>();
	private static boolean started = false;

	/**
	 * Dont call this unless you know what you are doing!
	 */
	public static void executeAfterStartupQueue()
	{
		if(started)
		{
			return;
		}

		started = true;

		for(Runnable r : afterStartup)
		{
			s(r);
		}

		for(Runnable r : afterStartupAsync)
		{
			a(r);
		}

		afterStartup = null;
		afterStartupAsync = null;
	}

	/**
	 * Schedule a sync task to be run right after startup. If the server has already
	 * started ticking, it will simply run it in a sync task.
	 *
	 * If you dont know if you should queue this or not, do so, it's pretty
	 * forgiving.
	 *
	 * @param r
	 *            the runnable
	 */
	public static void ass(Runnable r)
	{
		if(started)
		{
			s(r);
		}

		else
		{
			afterStartup.add(r);
		}
	}

	/**
	 * Schedule an async task to be run right after startup. If the server has
	 * already started ticking, it will simply run it in an async task.
	 *
	 * If you dont know if you should queue this or not, do so, it's pretty
	 * forgiving.
	 *
	 * @param r
	 *            the runnable
	 */
	public static void asa(Runnable r)
	{
		if(started)
		{
			a(r);
		}

		else
		{
			afterStartupAsync.add(r);
		}
	}

	/**
	 * Queue a sync task
	 *
	 * @param r
	 *            the runnable
	 */
	public static void s(Runnable r)
	{
		SVC.get(TaskSVC.class).queue(r);
	}

	/**
	 * Queue a sync task
	 *
	 * @param r
	 *            the runnable
	 * @param delay
	 *            the delay to wait in ticks before running
	 */
	public static void s(Runnable r, int delay)
	{
		SVC.get(TaskSVC.class).queueDelayed(r, delay);
	}

	/**
	 * Cancel a sync repeating task
	 *
	 * @param id
	 *            the task id
	 */
	public static void csr(int id)
	{
		SVC.get(TaskSVC.class).dequeueRepeating(id);
	}

	/**
	 * Start a sync repeating task
	 *
	 * @param r
	 *            the runnable
	 * @param interval
	 *            the interval
	 * @return the task id
	 */
	public static int sr(Runnable r, int interval)
	{
		return SVC.get(TaskSVC.class).queueRepeating(r, interval);
	}

	/**
	 * Start a sync repeating task for a limited amount of ticks
	 *
	 * @param r
	 *            the runnable
	 * @param interval
	 *            the interval in ticks
	 * @param intervals
	 *            the maximum amount of intervals to run
	 */
	public static void sr(Runnable r, int interval, int intervals)
	{
		FinalInteger fi = new FinalInteger(0);

		new SR()
		{
			@Override
			public void run()
			{
				fi.add(1);
				r.run();

				if(fi.get() >= intervals)
				{
					cancel();
				}
			}
		};
	}

	/**
	 * Call an async task
	 *
	 * @param r
	 *            the runnable
	 */
	public static void a(Runnable r)
	{
		SVC.get(PoolSVC.class).queue(r);
	}

	/**
	 * Call an async task dealyed
	 *
	 * @param r
	 *            the runnable
	 * @param delay
	 *            the delay to wait before running
	 */
	public static void a(Runnable r, int delay)
	{
		SVC.get(PoolSVC.class).queueDelayed(r, delay);
	}

	/**
	 * Cancel an async repeat task
	 *
	 * @param id
	 *            the id
	 */
	public static void car(int id)
	{
		SVC.get(PoolSVC.class).dequeueRepeating(id);
	}

	/**
	 * Start an async repeat task
	 *
	 * @param r
	 *            the runnable
	 * @param interval
	 *            the interval in ticks
	 * @return the task id
	 */
	public static int ar(Runnable r, int interval)
	{
		return SVC.get(PoolSVC.class).queueRepeating(r, interval);
	}

	/**
	 * Start an async repeating task for a limited time
	 *
	 * @param r
	 *            the runnable
	 * @param interval
	 *            the interval
	 * @param intervals
	 *            the intervals to run
	 */
	public static void ar(Runnable r, int interval, int intervals)
	{
		FinalInteger fi = new FinalInteger(0);

		new AR()
		{
			@Override
			public void run()
			{
				fi.add(1);
				r.run();

				if(fi.get() >= intervals)
				{
					cancel();
				}
			}
		};
	}

	/**
	 * Lazy-Sync. Executes eventually, not important etc
	 *
	 * @param r
	 *            the runnable
	 */
	public static void ls(Runnable r)
	{
		SVC.get(TaskSVC.class).queueLazy(r);
	}

	/**
	 * Lazy-Async. Executes eventually, not important etc
	 *
	 * @param r
	 *            the runnable
	 */
	public static void la(Runnable r)
	{
		SVC.get(PoolSVC.class).queueLazy(r);
	}
}

package com.volmit.phantom.api.job;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.lib.service.PoolSVC;
import com.volmit.phantom.lib.service.TaskSVC;

public class J
{
	private static final GList<Runnable> afterStartup = new GList<>();
	private static final GList<Runnable> afterStartupAsync = new GList<>();
	private static boolean started = false;

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
		aft
	}

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

	public static void s(Runnable r)
	{
		SVC.get(TaskSVC.class).queue(r);
	}

	public static void s(Runnable r, int delay)
	{
		SVC.get(TaskSVC.class).queueDelayed(r, delay);
	}

	public static void csr(int id)
	{
		SVC.get(TaskSVC.class).dequeueRepeating(id);
	}

	public static int sr(Runnable r, int interval)
	{
		return SVC.get(TaskSVC.class).queueRepeating(r, interval);
	}

	public static void a(Runnable r)
	{
		SVC.get(PoolSVC.class).queue(r);
	}

	public static void a(Runnable r, int delay)
	{
		SVC.get(PoolSVC.class).queueDelayed(r, delay);
	}

	public static void car(int id)
	{
		SVC.get(PoolSVC.class).dequeueRepeating(id);
	}

	public static int ar(Runnable r, int interval)
	{
		return SVC.get(PoolSVC.class).queueRepeating(r, interval);
	}
}

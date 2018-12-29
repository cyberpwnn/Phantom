package com.volmit.phantom.plugin;

import com.volmit.phantom.lang.GList;

public class JobService<T extends Job> extends ThreadedService
{
	private GList<T> jobs;

	public void queue(T t)
	{
		jobs.add(t);
	}

	@Override
	public void doStart()
	{
		jobs = new GList<>();
		setName(getClass().getSimpleName().replaceAll("SVC", "").replaceAll("Service", "") + " Job Dispatcher");
	}

	@Override
	public void doIntervalWork()
	{
		for(T i : jobs)
		{
			i.run();
		}

		jobs.clear();
	}

	@Override
	public void doStop()
	{

	}
}

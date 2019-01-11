package com.volmit.phantom.api.sheduler;

import com.volmit.phantom.api.job.J;

public abstract class AR implements Runnable, CancellableTask
{
	private int id = 0;

	public AR()
	{
		this(0);
	}

	public AR(int interval)
	{
		id = J.ar(this, interval);
	}

	@Override
	public void cancel()
	{
		J.car(id);
	}

	public int getId()
	{
		return id;
	}
}

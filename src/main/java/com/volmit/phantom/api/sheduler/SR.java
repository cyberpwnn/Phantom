package com.volmit.phantom.api.sheduler;

import com.volmit.phantom.api.job.J;

public abstract class SR implements Runnable
{
	private int id = 0;

	public SR()
	{
		this(0);
	}

	public SR(int interval)
	{
		id = J.sr(this, interval);
	}

	public void cancel()
	{
		J.csr(id);
	}

	public int getId()
	{
		return id;
	}
}

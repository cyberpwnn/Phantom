package com.volmit.phantom.api.sheduler;

import com.volmit.phantom.api.job.J;

public abstract class S implements Runnable
{
	public S()
	{
		J.s(this);
	}

	public S(int delay)
	{
		J.s(this, delay);
	}
}

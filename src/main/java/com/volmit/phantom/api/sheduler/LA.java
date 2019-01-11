package com.volmit.phantom.api.sheduler;

import com.volmit.phantom.api.job.J;

public abstract class LA implements Runnable
{
	public LA()
	{
		J.la(this);
	}
}

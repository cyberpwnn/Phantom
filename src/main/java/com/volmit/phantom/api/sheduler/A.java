package com.volmit.phantom.api.sheduler;

import com.volmit.phantom.api.job.J;

public abstract class A implements Runnable
{
	public A()
	{
		J.a(this);
	}

	public A(int delay)
	{
		J.a(this, delay);
	}
}

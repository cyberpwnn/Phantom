package com.volmit.phantom.plugin;

public abstract class S implements Runnable
{
	public static TaskManager m;

	public S()
	{
		m.sync(this);
	}

	public S(long delay)
	{
		m.sync(delay, this);
	}
}

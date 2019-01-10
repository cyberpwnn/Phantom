package com.volmit.phantom.api.sheduler;

import com.volmit.phantom.imp.plugin.TaskManager;

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

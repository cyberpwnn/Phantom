package com.volmit.phantom.api.sheduler;

import com.volmit.phantom.api.job.J;

public abstract class LS implements Runnable
{
	public LS()
	{
		J.ls(this);
	}
}

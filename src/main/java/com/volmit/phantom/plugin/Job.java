package com.volmit.phantom.plugin;

public abstract class Job implements Runnable
{
	public boolean shouldContinueWorking()
	{
		return !Thread.interrupted();
	}
}

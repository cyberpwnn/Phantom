package com.volmit.phantom.imp.plugin;

public abstract class Job implements Runnable
{
	public boolean shouldContinueWorking()
	{
		return !Thread.interrupted();
	}
}

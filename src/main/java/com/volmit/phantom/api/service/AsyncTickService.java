package com.volmit.phantom.api.service;

import com.volmit.phantom.api.job.J;

public abstract class AsyncTickService extends Service
{
	private int task;
	private int interval;

	public abstract void onBegin();

	public abstract void onEnd();

	public abstract void onAsyncTick();

	public AsyncTickService(int interval)
	{
		this.interval = interval;
	}

	@Override
	public void onStart()
	{
		task = J.ar(() -> onAsyncTick(), interval);
		onBegin();
	}

	@Override
	public void onStop()
	{
		J.car(task);
		onEnd();
	}
}

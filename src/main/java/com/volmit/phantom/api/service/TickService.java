package com.volmit.phantom.api.service;

import com.volmit.phantom.api.job.J;

public abstract class TickService extends Service
{
	private int task;
	private int interval;

	public abstract void onBegin();

	public abstract void onEnd();

	public abstract void onTick();

	public TickService(int interval)
	{
		this.interval = interval;
	}

	@Override
	public void onStart()
	{
		task = J.sr(() -> onTick(), interval);
		onBegin();
	}

	@Override
	public void onStop()
	{
		J.csr(task);
		onEnd();
	}
}

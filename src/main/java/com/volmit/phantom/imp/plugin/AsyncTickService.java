package com.volmit.phantom.imp.plugin;

import com.volmit.phantom.api.sheduler.AR;

public abstract class AsyncTickService extends SimpleService
{
	private AR ar;
	private int rate;

	public abstract void onBegin();

	public abstract void onEnd();

	public abstract void onAsyncTick();

	public AsyncTickService(int rate)
	{
		this.rate = rate;
	}

	@Override
	public void onStart()
	{
		onBegin();
		ar = new AR(rate)
		{
			@Override
			public void run()
			{
				onAsyncTick();
			}
		};
	}

	@Override
	public void onStop()
	{
		try
		{
			ar.cancel();
		}

		catch(Throwable e)
		{

		}

		onEnd();
	}
}

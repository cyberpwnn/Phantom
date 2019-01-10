package com.volmit.phantom.imp.plugin;

import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.service.IService;

public abstract class ThreadedService extends Thread implements IService
{
	private long sleeptime = 1000;

	@Override
	public void onStart()
	{
		setName(getClass().getSimpleName().replaceAll("SVC", "").replaceAll("Service", "") + " Service Worker");
		setPriority(MIN_PRIORITY);
		doStart();
		start();
	}

	@Override
	public void run()
	{
		while(!interrupted())
		{
			try
			{
				doIntervalWork();
			}

			catch(Throwable e)
			{
				e.printStackTrace();
				D.as(this).f("Encountered an exception.");
			}

			try
			{
				Thread.sleep(sleeptime);
			}

			catch(InterruptedException e)
			{

			}
		}
	}

	@Override
	public void onStop()
	{
		interrupt();

		try
		{
			join(3000);
		}

		catch(InterruptedException e)
		{
			System.out.println("Failed to properly stop threaded service " + getName() + " after 3 seconds. We gave up.");
		}

		doStop();
	}

	public abstract void doStart();

	public abstract void doStop();

	public abstract void doIntervalWork();
}

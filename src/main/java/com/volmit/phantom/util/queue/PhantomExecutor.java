package com.volmit.phantom.util.queue;

import com.volmit.phantom.api.job.J;
import com.volmit.phantom.api.sheduler.AR;
import com.volmit.phantom.api.sheduler.CancellableTask;
import com.volmit.phantom.api.sheduler.SR;

public abstract class PhantomExecutor<T> implements QueueExecutor<T>
{
	private boolean async;
	private int ticks;
	private Queue<T> q;
	private CancellableTask task;

	public abstract void execute(T t);

	@Override
	public void queue(Queue<T> t)
	{
		this.q = t;
	}

	@Override
	public Queue<T> getQueue()
	{
		return q;
	}

	@Override
	public void start()
	{
		J.ass(() ->
		{
			if(async)
			{
				task = new AR(ticks)
				{
					@Override
					public void run()
					{
						doUpdate();
					}
				};
			}

			else
			{
				task = new SR(ticks)
				{
					@Override
					public void run()
					{
						doUpdate();
					}
				};
			}
		});
	}

	@Override
	public void stop()
	{
		try
		{
			task.cancel();
		}

		catch(Throwable e)
		{

		}
	}

	@Override
	public void doUpdate()
	{
		while(q.hasNext())
		{
			execute(q.next());
		}
	}

	@Override
	public void async(boolean async)
	{
		this.async = async;
	}

	@Override
	public void interval(int ticks)
	{
		this.ticks = ticks;
	}
}

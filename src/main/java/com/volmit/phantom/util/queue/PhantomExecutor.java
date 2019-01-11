package com.volmit.phantom.util.queue;

import org.bukkit.util.Consumer;

import com.volmit.phantom.api.sheduler.AR;
import com.volmit.phantom.api.sheduler.CancellableTask;
import com.volmit.phantom.api.sheduler.SR;

public class PhantomExecutor<T> implements QueueExecutor<T>
{
	private boolean async;
	private int ticks;
	private Queue<T> q;
	private CancellableTask task;
	private final Consumer<T> consumer;

	public PhantomExecutor(Consumer<T> consumer)
	{
		this.consumer = consumer;
	}

	@Override
	public PhantomExecutor<T> queue(Queue<T> t)
	{
		this.q = t;
		return this;
	}

	@Override
	public Queue<T> getQueue()
	{
		return q;
	}

	@Override
	public PhantomExecutor<T> start()
	{
		task = async ? new AR(ticks)
		{
			@Override
			public void run()
			{
				update();
			}
		} : new SR(ticks)
		{
			@Override
			public void run()
			{
				update();
			}
		};

		return this;
	}

	@Override
	public PhantomExecutor<T> stop()
	{
		try
		{
			task.cancel();
		}

		catch(Throwable e)
		{

		}

		return this;
	}

	@Override
	public PhantomExecutor<T> update()
	{
		while(q.hasNext())
		{
			getConsumer().accept(q.next());
		}

		return this;
	}

	@Override
	public PhantomExecutor<T> async(boolean async)
	{
		this.async = async;
		return this;
	}

	@Override
	public PhantomExecutor<T> interval(int ticks)
	{
		this.ticks = ticks;
		return this;
	}

	public Consumer<T> getConsumer()
	{
		return consumer;
	}
}

package com.volmit.phantom.util.queue;

import org.bukkit.util.Consumer;

import com.volmit.phantom.api.math.M;

public class ThrottledExecutor<T> extends PhantomExecutor<T>
{
	public ThrottledExecutor(Consumer<T> consumer)
	{
		super(consumer);
	}

	@Override
	public PhantomExecutor<T> update()
	{
		int size = getQueue().size();
		int allow = M.iclip(((double) size / 1.125D), 1, 250);

		while(getQueue().hasNext() && allow > 0)
		{
			allow--;
			getConsumer().accept(getQueue().next());
		}

		return this;
	}
}

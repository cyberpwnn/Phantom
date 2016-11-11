package org.phantomapi.sync;

import org.phantomapi.core.EventRippler;

public abstract class I implements Runnable
{
	public I()
	{
		EventRippler.queue(this);
	}
	
	@Override
	public abstract void run();
	
	public void i()
	{
		run();
	}
}

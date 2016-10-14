package org.phantomapi.construct;

public abstract class Ghost extends PhantomPlugin
{
	@Override
	public void enable()
	{
		preStart();
	}

	@Override
	public void disable()
	{
		postStop();
	}
	
	public abstract void preStart();
	
	public abstract void onStart();
	
	public abstract void onStop();
	
	public abstract void postStop();
}

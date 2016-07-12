package org.cyberpwn.phantom.sync;

public class ExecutiveRunnable<T> implements Runnable
{
	private T next;
	private Boolean cancelled;
	private Boolean repeated;
	
	public void run(T next)
	{
		this.next = next;
		this.cancelled = false;
		this.repeated = false;
		run();
	}
	
	@Override
	public void run()
	{
		
	}
	
	public T next()
	{
		return next;
	}
	
	public void cancel()
	{
		this.cancelled = true;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void repeat()
	{
		this.repeated = true;
	}
	
	public boolean isRepeated()
	{
		return repeated;
	}
}

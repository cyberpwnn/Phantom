package org.cyberpwn.phantom.sync;

import java.util.Iterator;
import java.util.List;

public class ExecutiveIterator<T> implements Iterator<T>
{
	private Iterator<T> it;
	private ExecutiveRunnable<T> runnable;
	private Boolean cancelled;
	private T repeated;
	private Integer size;
	
	public ExecutiveIterator(List<T> it, ExecutiveRunnable<T> runnable)
	{
		this.it = it.iterator();
		this.size = it.size();
		this.runnable = runnable;
		this.cancelled = false;
		this.repeated = null;
	}

	@Override
	public boolean hasNext()
	{
		return it.hasNext() && !cancelled;
	}

	@Override
	public T next()
	{
		T t = null;
		
		if(repeated != null)
		{
			t = repeated;
		}
		
		else
		{
			t = it.next();
		}
		
		runnable.run(t);
	
		if(runnable.isCancelled())
		{
			cancelled = true;	
		}
		
		if(runnable.isRepeated())
		{
			repeated = t;
		}
		
		else
		{
			repeated = null;
		}
		
		return t;
	}
	
	public int size()
	{
		return size;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void cancel()
	{
		cancelled = true;
	}
}

package org.cyberpwn.phantom.sync;

import java.util.Iterator;
import java.util.List;

public class ExecutiveIterator<T> implements Iterator<T>
{
	private Iterator<T> it;
	private ExecutiveRunnable<T> runnable;
	
	public ExecutiveIterator(Iterator<T> it, ExecutiveRunnable<T> runnable)
	{
		this.it = it;
		this.runnable = runnable;
	}
	
	public ExecutiveIterator(List<T> it, ExecutiveRunnable<T> runnable)
	{
		this.it = it.iterator();
		this.runnable = runnable;
	}

	@Override
	public boolean hasNext()
	{
		return it.hasNext();
	}

	@Override
	public T next()
	{
		T t = it.next();
		runnable.run(t);
		
		return t;
	}
}

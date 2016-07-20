package org.cyberpwn.phantom.sync;

import java.util.Iterator;
import java.util.List;

/**
 * An iterator which iterates through items in a list, but executes a runnable
 * per.
 * 
 * @author cyberpwn
 *
 * @param <T>
 *            the type of element to iterate through
 */
public class ExecutiveIterator<T> implements Iterator<T>
{
	private Iterator<T> it;
	private ExecutiveRunnable<T> runnable;
	private Boolean cancelled;
	private T repeated;
	private Integer size;
	
	/**
	 * Create an iterator
	 * 
	 * @param it
	 *            the list of items
	 * @param runnable
	 *            the executive runnable to iterate through the elements
	 */
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
	
	/**
	 * Size of the iterator
	 * 
	 * @return
	 */
	public int size()
	{
		return size;
	}
	
	/**
	 * Was the iterator cancelled
	 * 
	 * @return true if cancelled
	 */
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	/**
	 * Cancel the iterator from running any more elements
	 */
	public void cancel()
	{
		cancelled = true;
	}
}

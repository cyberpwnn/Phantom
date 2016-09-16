package org.phantomapi.sync;

import java.util.Iterator;
import java.util.List;
import org.phantomapi.core.ChanneledExecutivePoolController;

/**
 * An iterator which iterates through items in a list, but executes a runnable
 * per.
 * 
 * @author cyberpwn
 *
 * @param <T>
 *            the type of element to iterate through
 */
public abstract class ExecutiveIterator<T> implements Iterator<T>
{
	private Iterator<T> it;
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
	public ExecutiveIterator(List<T> it)
	{
		this.it = it.iterator();
		this.size = it.size();
		this.cancelled = false;
		this.repeated = null;
	}
	
	/**
	 * Create an iterator
	 * 
	 * @param it
	 *            the iterator
	 * @param runnable
	 *            the executive runnable to iterate through the elements
	 */
	public ExecutiveIterator(Iterator<T> it)
	{
		this.it = it;
		this.size = 1337;
		this.cancelled = false;
		this.repeated = null;
	}
	
	public abstract void onIterate(T next);
	
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
		
		ChanneledExecutivePoolController.hit++;
		onIterate(t);
		
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

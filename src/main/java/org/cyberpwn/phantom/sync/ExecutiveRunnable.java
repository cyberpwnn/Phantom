package org.cyberpwn.phantom.sync;

/**
 * The runnable callback for all iterators when next is fired
 * 
 * @author cyberpwn
 *
 * @param <T>
 *            the iterator type
 */
public class ExecutiveRunnable<T> implements Runnable
{
	private T next;
	private Boolean cancelled;
	private Boolean repeated;
	
	/**
	 * Run the next iterable element
	 * 
	 * @param next
	 *            the nexted object
	 */
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
	
	/**
	 * The next object, calling this multiple times will yeild the same object
	 * in the same runnable iteration
	 * 
	 * @return the element
	 */
	public T next()
	{
		return next;
	}
	
	/**
	 * Signal cancel to iterator
	 */
	public void cancel()
	{
		this.cancelled = true;
	}
	
	/**
	 * Is there a signal to cancel?
	 * 
	 * @return true if cancelled
	 */
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	/**
	 * Repeat this current element in the next iteration, then continue to the
	 * next element after that as normal.
	 */
	public void repeat()
	{
		this.repeated = true;
	}
	
	/**
	 * Is there a signal to repeat?
	 * 
	 * @return true if yes
	 */
	public boolean isRepeated()
	{
		return repeated;
	}
}

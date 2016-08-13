package org.phantomapi.sync;

import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.util.M;

/**
 * Consists of multiple executive tasks to be spliced into one iterator limit
 * 
 * @author cyberpwn
 *
 */
public class ExecutivePool
{
	private GList<ExecutiveIterator<?>> tasks;
	private Task task;
	
	/**
	 * Create a new pool of iterators
	 * 
	 * @param limit
	 *            the limit in milliseconds per interval
	 * @param interval
	 *            the interval in ticks
	 */
	public ExecutivePool(Double limit, Integer interval)
	{
		tasks = new GList<ExecutiveIterator<?>>();
		task = new Task(Phantom.instance(), interval)
		{
			public void run()
			{
				if(tasks.isEmpty())
				{
					return;
				}
				
				long ns = M.ns();
				
				while(!tasks.isEmpty() && ((double) (M.ms() - ns) / 1000000.0) < limit)
				{
					for(ExecutiveIterator<?> i : tasks.copy())
					{
						if(!tasks.isEmpty() && ((double) (M.ms() - ns) / 1000000.0) < limit)
						{
							if(i.hasNext())
							{
								i.next();
							}
							
							else
							{
								tasks.remove(i);
							}
						}
					}
				}
			}
		};
	}
	
	/**
	 * Is the pool idle? Doing nothing.
	 * 
	 * @return true if nothing is going on
	 */
	public boolean isIdle()
	{
		return tasks.isEmpty();
	}
	
	/**
	 * Add a new executive iterator (any type)
	 * 
	 * @param it
	 *            the iterator
	 */
	public void add(ExecutiveIterator<?> it)
	{
		tasks.add(it);
	}
	
	/**
	 * Get the size of all elements in all iterators
	 * 
	 * @return the size
	 */
	public int size()
	{
		int s = 0;
		
		for(ExecutiveIterator<?> i : tasks.copy())
		{
			s += i.size();
		}
		
		return s;
	}
	
	/**
	 * Stop all tasks and the parent task
	 */
	public void cancel()
	{
		for(ExecutiveIterator<?> i : tasks)
		{
			i.cancel();
		}
		
		task.cancel();
	}
	
	/**
	 * Is the iterator running?
	 * 
	 * @return true if running or idling
	 */
	public boolean isRunning()
	{
		return task.isRunning();
	}
}

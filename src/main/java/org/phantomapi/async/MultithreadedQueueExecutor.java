package org.phantomapi.async;

import org.phantomapi.lang.GList;
import org.phantomapi.util.A;
import org.phantomapi.util.S;

/**
 * Create a multithreaded queue executor. This allows you to pile in objects and
 * process each one just like an executive iterator, except this allows multiple
 * or just one async thread working on the queue at once. Ensure that the object
 * T implements the equals method
 * 
 * @author cyberpwn
 * @param <T>
 *            the object type for the queue. Ensure it implements the equals
 *            method.
 */
public abstract class MultithreadedQueueExecutor<T>
{
	private GList<T> queue;
	private GList<T> active;
	private int maxThreads;
	
	/**
	 * Create a multithreaded queued executor with the desired max threads
	 * 
	 * @param maxThreads
	 *            the max threads. This can be changed at anytime.
	 */
	public MultithreadedQueueExecutor(int maxThreads)
	{
		this.queue = new GList<T>();
		this.active = new GList<T>();
		this.maxThreads = maxThreads;
	}
	
	/**
	 * This is called async. Any sync invocations should be handled as this is
	 * called async
	 * 
	 * @param t
	 *            the object being processed
	 */
	public abstract void onProcess(T t);
	
	/**
	 * Queue an object for processing. This method forces sync appending to the
	 * queue. If you queue this async, a sync task will be queued to queue your
	 * object. safe to be called async or sync
	 * 
	 * @param t
	 *            the object to queue for multithreaded processing
	 */
	public void queue(T t)
	{
		new S()
		{
			@Override
			public void sync()
			{
				queue.add(t);
			}
		};
	}
	
	/**
	 * This method dispatches new threads to work on objects within the queue.
	 * This object does not automatically tick itself. Tick this at the desired
	 * interval to make it actually process the objects in the queue safe to be
	 * called async or sync
	 */
	public void dispatch()
	{
		new S()
		{
			@Override
			public void sync()
			{
				while(!queue.isEmpty() && active.size() < maxThreads)
				{
					T t = queue.pop();
					dispatchThread(t);
				}
			}
		};
	}
	
	/**
	 * Dispatches a thread into the pool with the given object to work with safe
	 * to be called async or sync
	 * 
	 * @param t
	 *            the object
	 */
	public void dispatchThread(T t)
	{
		new S()
		{
			@Override
			public void sync()
			{
				active.add(t);
				
				new A()
				{
					@Override
					public void async()
					{
						onProcess(t);
						
						new S()
						{
							@Override
							public void sync()
							{
								active.remove(t);
							}
						};
					}
				};
			}
		};
	}
	
	/**
	 * Get the queue ENFORCES SYNC
	 * 
	 * @return the queued objects to be processed
	 */
	public GList<T> getQueue()
	{
		AsyncUtil.enforceSync();
		return queue;
	}
	
	/**
	 * Get the active queue ENFORCES SYNC
	 * 
	 * @return the active objects being processed
	 */
	public GList<T> getActive()
	{
		AsyncUtil.enforceSync();
		return active;
	}
	
	/**
	 * Get the max threads
	 * 
	 * @return the max threads
	 */
	public int getMaxThreads()
	{
		return maxThreads;
	}
	
	/**
	 * Set the max threads. If there are threads currently working at this time,
	 * once they finish the new dispatched threads will follow the updated max.
	 * ENFORCES SYNC
	 * 
	 * @param maxThreads
	 *            the new max threads count.
	 */
	public void setMaxThreads(int maxThreads)
	{
		AsyncUtil.enforceSync();
		this.maxThreads = maxThreads;
	}
	
	/**
	 * Get the active queue size ENFORCES SYNC
	 * 
	 * @return the size of active objects being processed
	 */
	public int getActiveThreads()
	{
		AsyncUtil.enforceSync();
		return active.size();
	}
	
	/**
	 * Clear the queue. Async calls will be scheduled to be invoked sync
	 */
	public void clearQueue()
	{
		new S()
		{
			@Override
			public void sync()
			{
				queue.clear();
			}
		};
	}
}

package org.phantomapi.sync;

import org.phantomapi.Phantom;
import org.phantomapi.util.M;

/**
 * An executive task to iterate through each element in an executive iterator as
 * fast as possible without exeeding the ms limit
 * 
 * @author cyberpwn
 *
 * @param <T>
 *            the element type
 */
public class ExecutiveTask<T>
{
	private ExecutiveIterator<T> it;
	private Double limit;
	private Integer interval;
	private Runnable finish;
	private Task task;
	
	/**
	 * New iterator start
	 * 
	 * @param it
	 *            the executive iterator
	 * @param limit
	 *            the millisecond limit per interval
	 * @param interval
	 *            the interval in ticks
	 * @param finish
	 *            the finish runnable to be called when finished
	 */
	public ExecutiveTask(ExecutiveIterator<T> it, Double limit, Integer interval, Runnable finish)
	{
		this.it = it;
		this.limit = limit;
		this.interval = interval;
		this.finish = finish;
		
		this.task = new Task(Phantom.instance(), ExecutiveTask.this.interval)
		{
			public void run()
			{
				long ns = M.ns();
				
				while(ExecutiveTask.this.it.hasNext() && ((double) (M.ms() - ns) / 1000000.0) < ExecutiveTask.this.limit)
				{
					ExecutiveTask.this.it.next();
				}
				
				if(!ExecutiveTask.this.it.hasNext())
				{
					ExecutiveTask.this.finish.run();
					cancel();
				}
			}
		};
	}
	
	/**
	 * Stop the iterator
	 */
	public void cancel()
	{
		it.cancel();
		task.cancel();
	}
	
	/**
	 * Is it running?
	 * 
	 * @return true if running
	 */
	public boolean isRunning()
	{
		return task.isRunning();
	}
}

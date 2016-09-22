package org.phantomapi.sync;

import org.phantomapi.Phantom;
import org.phantomapi.construct.Controllable;

/**
 * Fast access to the scheduler
 * 
 * @author cyberpwn
 */
public abstract class Task implements Runnable
{
	public static int taskx = 0;
	private Controllable pl;
	private Integer[] task;
	private Boolean running;
	
	/**
	 * Create a task under the controllable object
	 * 
	 * @param pl
	 *            the controllable object
	 * @param interval
	 *            the interval
	 */
	public Task(Controllable pl, int interval)
	{
		taskx++;
		this.pl = pl;
		this.running = true;
		this.task = new Integer[] {pl.getPlugin().scheduleSyncRepeatingTask(0, interval, this)};
	}
	
	/**
	 * Create a new repeating task
	 * 
	 * @param interval
	 *            the interval
	 */
	public Task(int interval)
	{
		taskx++;
		this.pl = Phantom.instance();
		this.running = true;
		this.task = new Integer[] {pl.getPlugin().scheduleSyncRepeatingTask(0, interval, this)};
	}
	
	/**
	 * Create a repeating task with a max interval
	 * 
	 * @param interval
	 *            the interval
	 * @param intervals
	 *            the maximum intervals before it is cancelled
	 */
	public Task(int interval, int intervals)
	{
		int[] k = new int[] {0};
		
		new Task(interval)
		{
			@Override
			public void run()
			{
				if(k[0] > intervals)
				{
					Task.this.cancel();
					cancel();
					
					return;
				}
				
				Task.this.run();
				k[0]++;
				
				if(!Task.this.running)
				{
					cancel();
				}
			}
		};
	}
	
	@Override
	public abstract void run();
	
	/**
	 * Cancel the task
	 */
	public void cancel()
	{
		running = false;
		pl.getPlugin().cancelTask(task[0]);
		taskx--;
	}
	
	/**
	 * Is it running?
	 * 
	 * @return true if running
	 */
	public boolean isRunning()
	{
		return running;
	}
}

package org.cyberpwn.phantom.sync;

import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.construct.Controllable;

/**
 * Fast access to the scheduler
 * 
 * @author cyberpwn
 *
 */
public class Task implements Runnable
{
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
		this.pl = pl;
		this.running = true;
		this.task = new Integer[] { pl.getPlugin().scheduleSyncRepeatingTask(0, interval, this) };
	}
	
	/**
	 * Create a new repeating task
	 * 
	 * @param interval
	 *            the interval
	 */
	public Task(int interval)
	{
		this.pl = Phantom.instance();
		this.running = true;
		this.task = new Integer[] { pl.getPlugin().scheduleSyncRepeatingTask(0, interval, this) };
	}
	
	@Override
	public void run()
	{
		
	}
	
	/**
	 * Cancel the task
	 */
	public void cancel()
	{
		running = false;
		pl.getPlugin().cancelTask(task[0]);
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

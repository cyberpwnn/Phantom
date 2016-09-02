package org.phantomapi.sync;

import org.phantomapi.Phantom;
import org.phantomapi.construct.Controllable;

/**
 * Fast access to the scheduler
 * 
 * @author cyberpwn
 *
 */
public abstract class TaskLater implements Runnable
{
	public static int taskx = 0;
	private Controllable pl;
	
	/**
	 * Creates a scheduled sync task
	 * 
	 * @param pl
	 *            the controllable
	 */
	public TaskLater(Controllable pl)
	{
		this.pl = pl;
		pl.getPlugin().scheduleSyncTask(0, this);
		taskx++;
	}
	
	/**
	 * Run in the next tick
	 */
	public TaskLater()
	{
		this.pl = Phantom.instance();
		pl.getPlugin().scheduleSyncTask(0, this);
		taskx++;
	}
	
	/**
	 * Run after a delay
	 * 
	 * @param delay
	 *            the delay in ticks
	 */
	public TaskLater(Integer delay)
	{
		this.pl = Phantom.instance();
		pl.getPlugin().scheduleSyncTask(delay, this);
		taskx++;
	}
	
	@Override
	public abstract void run();
}

package org.cyberpwn.phantom.sync;

import org.cyberpwn.phantom.construct.Controllable;

public class Task implements Runnable
{
	private Controllable pl;
	private int[] task;
	
	public Task(Controllable pl, int interval)
	{
		this.pl = pl;
		this.task = new int[]{pl.getPlugin().scheduleSyncRepeatingTask(0, interval, this)};
	}
	
	@Override
	public void run()
	{
		
	}
	
	public void cancel()
	{
		pl.getPlugin().cancelTask(task[0]);
	}
}

package org.cyberpwn.phantom.sync;

import org.cyberpwn.phantom.construct.Controllable;

public class Task implements Runnable
{
	private Controllable pl;
	private Integer[] task;
	private Boolean running;
	
	public Task(Controllable pl, int interval)
	{
		this.pl = pl;
		this.running = true;
		this.task = new Integer[]{pl.getPlugin().scheduleSyncRepeatingTask(0, interval, this)};
	}
	
	@Override
	public void run()
	{
		
	}
	
	public void cancel()
	{
		running = false;
		pl.getPlugin().cancelTask(task[0]);
	}
	
	public boolean isRunning()
	{
		return running;
	}
}

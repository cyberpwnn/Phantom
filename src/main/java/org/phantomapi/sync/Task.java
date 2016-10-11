package org.phantomapi.sync;

import org.bukkit.plugin.IllegalPluginAccessException;
import org.phantomapi.Phantom;
import org.phantomapi.construct.Controllable;
import org.phantomapi.util.D;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.FinalInteger;

/**
 * Fast access to the scheduler
 * 
 * @author cyberpwn
 */
public abstract class Task implements Runnable
{
	public static int taskx = 0;
	private Controllable pl;
	private FinalInteger task;
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
		new S()
		{
			@Override
			public void sync()
			{
				taskx++;
				Task.this.pl = pl;
				running = true;
				
				try
				{
					task = new FinalInteger(pl.getPlugin().scheduleSyncRepeatingTask(0, interval, Task.this));
				}
				
				catch(IllegalPluginAccessException e)
				{
					new D("Task").f("Could not create plugin. It's disabled.");
					ExceptionUtil.print(e);
				}
				
				catch(Exception e)
				{
					new D("Exception: " + e.getClass().getSimpleName());
					ExceptionUtil.print(e);
				}
			}
		};
	}
	
	/**
	 * Create a new repeating task
	 * 
	 * @param interval
	 *            the interval
	 */
	public Task(int interval)
	{
		this(Phantom.instance(), interval);
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
		new S()
		{
			@Override
			public void sync()
			{
				FinalInteger k = new FinalInteger(0);
				try
				{
					task = new FinalInteger(-1);
				}
				
				catch(IllegalPluginAccessException e)
				{
					new D("Task").f("Could not create plugin. It's disabled.");
					ExceptionUtil.print(e);
				}
				
				catch(Exception e)
				{
					new D("Exception: " + e.getClass().getSimpleName());
					ExceptionUtil.print(e);
				}
				
				running = true;
				
				new Task(interval)
				{
					@Override
					public void run()
					{
						if(k.get() > intervals)
						{
							cancel();
							
							return;
						}
						
						Task.this.run();
						k.add(1);
						
						if(!Task.this.running)
						{
							cancel();
						}
					}
				};
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
		pl.getPlugin().cancelTask(task.get());
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

package org.phantomapi.sync;

import org.bukkit.plugin.IllegalPluginAccessException;
import org.phantomapi.Phantom;
import org.phantomapi.construct.Controllable;
import org.phantomapi.util.D;
import org.phantomapi.util.ExceptionUtil;

/**
 * Fast access to the scheduler
 * 
 * @author cyberpwn
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
		new S()
		{
			@Override
			public void sync()
			{
				TaskLater.this.pl = pl;
				
				try
				{
					pl.getPlugin().scheduleSyncTask(0, TaskLater.this);
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
				
				taskx++;
			}
		};
	}
	
	/**
	 * Run in the next tick
	 */
	public TaskLater()
	{
		new S()
		{
			@Override
			public void sync()
			{
				pl = Phantom.instance();
				
				try
				{
					pl.getPlugin().scheduleSyncTask(0, TaskLater.this);
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
				
				taskx++;
			}
		};
	}
	
	/**
	 * Run after a delay
	 * 
	 * @param delay
	 *            the delay in ticks
	 */
	public TaskLater(Integer delay)
	{
		new S()
		{
			@Override
			public void sync()
			{
				pl = Phantom.instance();
				
				try
				{
					pl.getPlugin().scheduleSyncTask(delay, TaskLater.this);
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
				
				taskx++;
			}
		};
	}
	
	@Override
	public abstract void run();
}

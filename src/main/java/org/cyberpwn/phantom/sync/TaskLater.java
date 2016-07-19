package org.cyberpwn.phantom.sync;

import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.construct.Controllable;

public class TaskLater implements Runnable
{
	private Controllable pl;
	
	public TaskLater(Controllable pl)
	{
		this.pl = pl;
		pl.getPlugin().scheduleSyncTask(0, this);
	}
	
	public TaskLater()
	{
		this.pl = Phantom.instance();
		pl.getPlugin().scheduleSyncTask(0, this);
	}
	
	public TaskLater(Integer delay)
	{
		this.pl = Phantom.instance();
		pl.getPlugin().scheduleSyncTask(delay, this);
	}

	@Override
	public void run()
	{
		
	}
}

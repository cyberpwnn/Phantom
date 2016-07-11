package org.cyberpwn.phantom.sync;

import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.M;

public class ExecutivePool
{
	private GList<ExecutiveIterator<?>> tasks;
	private Task task;
	
	public ExecutivePool(Double limit, Integer interval)
	{
		tasks = new GList<ExecutiveIterator<?>>();
		task = new Task(Phantom.instance(), interval)
		{
			public void run()
			{
				if(tasks.isEmpty())
				{
					return;
				}
				
				long ns = M.ns();
				
				for(ExecutiveIterator<?> i : tasks.copy())
				{
					if(!tasks.isEmpty() && ((double)(M.ms() - ns) / 1000000.0) < limit)
					{
						if(i.hasNext())
						{
							i.next();
						}
						
						else
						{
							tasks.remove(i);
						}
					}
				}
			}
		};
	}
	
	public void add(ExecutiveIterator<?> it)
	{
		tasks.add(it);
	}
	
	public void cancel()
	{
		task.cancel();
	}
	
	public boolean isRunning()
	{
		return task.isRunning();
	}
}

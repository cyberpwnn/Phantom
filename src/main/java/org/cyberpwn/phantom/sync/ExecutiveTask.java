package org.cyberpwn.phantom.sync;

import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.util.M;

public class ExecutiveTask<T>
{
	private ExecutiveIterator<T> it;
	private Double limit;
	private Integer interval;
	private Runnable finish;
	private Task task;
	
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
				
				while(ExecutiveTask.this.it.hasNext() && ((double)(M.ms() - ns) / 1000000.0) < ExecutiveTask.this.limit)
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
	
	public void cancel()
	{
		it.cancel();
		task.cancel();
	}
	
	public boolean isRunning()
	{
		return task.isRunning();
	}
}

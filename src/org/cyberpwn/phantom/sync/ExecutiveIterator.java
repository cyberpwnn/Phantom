package org.cyberpwn.phantom.sync;

import java.util.Iterator;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.M;

public class ExecutiveIterator<T>
{
	private Iterator<T> it;
	
	public ExecutiveIterator(Controllable controller, Long lim, GList<T> data, ExecutiveRunnable<T> runnable, Runnable finish)
	{
		this.it = data.iterator();
		
		new Task(controller, 0)
		{
			public void run()
			{
				Long ms = M.ms();
				
				while(it.hasNext() && M.ms() - ms < lim)
				{
					runnable.run(it.next());
				}
				
				if(!it.hasNext())
				{
					finish.run();
					cancel();
				}
			}
		};
	}
}

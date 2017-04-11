package org.phantomapi.util;

import org.phantomapi.lang.GList;

public class TXE extends Thread
{
	private GList<Runnable> run;
	private GList<Runnable> q;
	private long activeTime;
	private long inactiveTime;
	private Average usage;
	
	public TXE()
	{
		run = new GList<Runnable>();
		q = run.copy();
		inactiveTime = 0;
		activeTime = 0;
		usage = new Average(12);
	}
	
	public void add(Runnable r)
	{
		q.add(r);
	}
	
	public void run()
	{
		while(!interrupted())
		{
			Timer t = new Timer();
			t.start();
			
			run.add(q.copy());
			q.clear();
			
			while(!run.isEmpty())
			{
				try
				{
					run.pop().run();
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			t.stop();
			activeTime += t.getTime();
			
			try
			{
				Thread.sleep(50);
				inactiveTime += 50000000;
			}
			
			catch(InterruptedException e)
			{
				
			}
			
			usage.put((double) activeTime / (double) ((double) activeTime + (double) inactiveTime));
			activeTime = 0;
			inactiveTime = 0;
		}
	}
	
	public String status()
	{
		return q.size() + " > " + run.size() + " (" + F.pc(usage.getAverage(), 0) + ")";
	}
}

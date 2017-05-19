package org.phantomapi.coretick;

import org.phantomapi.lang.GList;

public class ParallelPoolManager
{
	private QueueMode mode;
	private GList<ParallelThread> threads;
	private int next;
	private int threadCount;
	private int lt;
	
	public ParallelPoolManager(int threadCount, QueueMode mode)
	{
		if(threadCount < 1)
		{
			threadCount = 1;
		}
		
		if(threadCount > 4)
		{
			System.out.println("WARNING: HIGH THREAD COUNT FOR CORETICK");
		}
		
		lt = 0;
		threads = new GList<ParallelThread>();
		this.threadCount = threadCount;
		next = 0;
		this.mode = mode;
	}
	
	public void start()
	{
		createThreads(threadCount);
	}
	
	public void check()
	{
		if(threads.size() != threadCount)
		{
			System.out.println("Invalid Pool, Resetting");
			restart();
		}
	}
	
	public void restart()
	{
		shutdown();
		start();
	}
	
	public void shutdown()
	{
		for(ParallelThread i : threads)
		{
			i.interrupt();
		}
		
		threads.clear();
	}
	
	public ParallelPoolManager(int threadCount)
	{
		this(threadCount, QueueMode.ROUND_ROBIN);
	}
	
	public void chk()
	{
		try
		{
			if(lt > 20)
			{
				lt = 0;
				check();
			}
			
			lt++;
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	public void queue(Execution e)
	{
		if(threads.isEmpty())
		{
			e.run();
			return;
		}
		
		nextThread().queue(e);
	}
	
	public int getSize()
	{
		return threads.size();
	}
	
	public ParallelThread[] getThreads()
	{
		return threads.toArray(new ParallelThread[threads.size()]);
	}
	
	private ParallelThread nextThread()
	{
		if(threads.size() == 1)
		{
			return threads.get(0);
		}
		
		int id = 0;
		
		switch(mode)
		{
			case ROUND_ROBIN:
				next = (next >= threads.size() ? 0 : next + 1);
				id = next;
			case SMALLEST:
				int min = Integer.MAX_VALUE;
				
				for(ParallelThread i : threads)
				{
					int size = i.getQueue().size();
					
					if(size < min)
					{
						min = size;
						id = i.getInfo().getId();
					}
				}
				
			default:
				break;
		}
		
		return threads.get(id);
	}
	
	private void createThreads(int count)
	{
		for(int i = 0; i < count; i++)
		{
			ParallelThread p = new ParallelThread(i);
			p.start();
			threads.add(p);
			System.out.println("Started Parallel CoreTick Thread: " + i);
		}
	}
}

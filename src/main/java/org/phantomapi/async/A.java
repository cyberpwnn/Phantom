package org.phantomapi.async;

import org.phantomapi.Phantom;
import org.phantomapi.lang.GMap;

/**
 * Create a fast async execution. Will not create a new async thread if the
 * current thread is already async
 * 
 * @author cyberpwn
 */
public abstract class A
{
	public static long tick = 0;
	public static int threads = 0;
	public static GMap<Integer, A> tasks = new GMap<Integer, A>();
	public static int id = 0;
	
	/**
	 * Create a fast async execution. Must implement the async() method
	 * (abstract)
	 */
	public A()
	{
		threads++;
		id++;
		
		int vid = id;
		tasks.put(vid, this);
		
		Phantom.async(new Runnable()
		{
			@Override
			public void run()
			{
				async();
				tasks.remove(vid);
			}
		});
	}
	
	/**
	 * Called and ran on a thread pool
	 */
	public abstract void async();
	
	/**
	 * Sleep until this thread is running parallel with the main thread
	 */
	public void rebase()
	{
		if(!Phantom.isAsync())
		{
			return;
		}
		
		long ct = tick;
		
		while(tick == ct)
		{
			try
			{
				Thread.sleep(1);
			}
			
			catch(InterruptedException e)
			{
				
			}
		}
	}
}

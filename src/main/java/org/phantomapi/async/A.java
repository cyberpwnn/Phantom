package org.phantomapi.async;

import org.phantomapi.Phantom;

/**
 * Create a fast async execution. Will not create a new async thread if the
 * current thread is already async
 * 
 * @author cyberpwn
 */
public abstract class A
{
	public static int threads = 0;
	
	/**
	 * Create a fast async execution. Must implement the async() method (abstract)
	 */
	public A()
	{
		threads++;
		if(Phantom.isAsync())
		{
			async();
			
		}
		
		else
		{
			Phantom.async(new Runnable()
			{
				@Override
				public void run()
				{
					async();
				}
			});
		}
	}
	
	public abstract void async();
}

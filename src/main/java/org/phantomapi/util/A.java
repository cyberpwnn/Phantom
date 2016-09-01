package org.phantomapi.util;

import org.phantomapi.Phantom;

/**
 * Create a fast async execution
 * 
 * @author cyberpwn
 */
public abstract class A
{
	/**
	 * Create a fast async execution
	 */
	public A()
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
	
	public abstract void async();
}

package org.phantomapi.util;

import org.phantomapi.Phantom;

/**
 * Fast Sync access
 * 
 * @author cyberpwn
 */
public abstract class S
{
	/**
	 * Create a sync execution
	 */
	public S()
	{
		Phantom.sync(new Runnable()
		{
			@Override
			public void run()
			{
				sync();
			}
		});
	}
	
	public abstract void sync();
}

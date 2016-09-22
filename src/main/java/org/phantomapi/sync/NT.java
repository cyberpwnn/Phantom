package org.phantomapi.sync;

/**
 * Run on the next tick
 * 
 * @author cyberpwn
 */
public abstract class NT
{
	public NT()
	{
		new TaskLater()
		{
			@Override
			public void run()
			{
				nextTick();
			}
		};
	}
	
	/**
	 * This will be called in the next tick
	 */
	public abstract void nextTick();
}

package org.cyberpwn.phantom.async;

/**
 * An async worker
 * 
 * @author cyberpwn
 */
public abstract class AsyncWorker extends Thread
{
	/**
	 * Do async work
	 */
	public AsyncWorker()
	{
		prepare();
	}
	
	/**
	 * Load in variables to the anonymous class for async use.
	 */
	public abstract void prepare();
	
	/**
	 * Do async work
	 */
	public abstract void doWork();
	
	/**
	 * Finish (callback)
	 */
	public abstract void finish();
	
	public void run()
	{
		doWork();
		finish();
	}
}

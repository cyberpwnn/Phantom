package org.cyberpwn.phantom.async;

/**
 * Schedule async runnable with a callback of the given type
 * 
 * @author cyberpwn
 *
 * @param <T>
 *            the given type.
 */
public class AsyncTask<T> extends Thread
{
	private Callback<T> callback;
	
	/**
	 * Create a new AsyncTask with a callback. You need to override the
	 * execute() method to make this task actually do someting, else Execute
	 * will return null. Override via anonymous class or a seperate class.
	 * 
	 * @param callback
	 *            the callback object. Must be the same type as <T>
	 */
	public AsyncTask(Callback<T> callback)
	{
		this.callback = callback;
	}
	
	/**
	 * Override this to add functionality. This requires that you retrn the
	 * callback object. Do not call the callback.
	 * 
	 * @return the object to be called back.
	 */
	public T execute()
	{
		return null;
	}
	
	public void run()
	{
		callback.run(execute());
	}
}

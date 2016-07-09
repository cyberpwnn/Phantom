package org.cyberpwn.phantom.async;

public class Callback<T> implements Runnable
{
	private T t;
	
	public void run(T t)
	{
		this.t = t;
		run();
	}
	
	@Override
	public void run()
	{
		
	}
	
	public T get()
	{
		return t;
	}
}

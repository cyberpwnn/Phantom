package com.volmit.phantom.plugin;

public abstract class A implements Runnable
{
	public static TaskManager m;

	public A()
	{
		m.async(this);
	}

	public A(long delay)
	{
		m.async(delay, this);
	}
}

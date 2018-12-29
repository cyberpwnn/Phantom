package com.volmit.phantom.plugin;

public class SVC
{
	public static <T extends IService> T get(Class<? extends T> serviceClass)
	{
		return Phantom.getService(serviceClass);
	}

	public static void start(Class<? extends IService> serviceClass)
	{
		get(serviceClass);
	}
}

package com.volmit.phantom.api.service;

import com.volmit.phantom.imp.plugin.Phantom;

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

	public static boolean isRunning(Class<? extends IService> c)
	{
		return Phantom.getRunningServices().contains(c);
	}
}

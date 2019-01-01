package com.volmit.phantom.plugin;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.text.C;

public class Phantom
{
	protected static TaskManager taskManager;
	protected static ModuleManager moduleManager;
	private static boolean started = false;
	private static final GList<Runnable> startupDump = new GList<Runnable>();
	private static final GMap<String, Integer> LOG_BUFFER = new GMap<String, Integer>();
	private static final GList<String> LOG_ORDER = new GList<String>();
	private static final GMap<Class<? extends IService>, IService> runningServices = new GMap<Class<? extends IService>, IService>();

	@SuppressWarnings("unchecked")
	public static <T extends IService> T getService(Class<? extends T> serviceClass)
	{
		try
		{
			if(!runningServices.containsKey(serviceClass))
			{
				D.as("Phantom > Service Provider").l("Starting Service: " + serviceClass.getSimpleName());
				IService s = serviceClass.getConstructor().newInstance();
				try
				{
					s.onStart();
				}

				catch(Throwable e)
				{
					D.as("Service Provider").w(s.getClass().getSimpleName() + " may have failed to properly start!");
				}

				runningServices.put(serviceClass, s);
			}
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return (T) runningServices.get(serviceClass);
	}

	public static TaskManager getTaskManager()
	{
		return taskManager;
	}

	public static ModuleManager getModuleManager()
	{
		return moduleManager;
	}

	public static GMap<String, Integer> getLOG_BUFFER()
	{
		return LOG_BUFFER;
	}

	public static GList<String> getLOG_ORDER()
	{
		return LOG_ORDER;
	}

	public static boolean isMainThread()
	{
		return Bukkit.isPrimaryThread();
	}

	public static void dumpStartup()
	{
		if(!started)
		{
			started = true;

			for(Runnable i : startupDump)
			{
				try
				{
					i.run();
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}

			startupDump.clear();
		}
	}

	public static void afterStartup(Runnable r)
	{
		if(started)
		{
			r.run();
		}

		else
		{
			startupDump.add(r);
		}
	}

	public static void flushLogBuffer()
	{
		if(!isMainThread())
		{
			throw new RuntimeException("Logs can only be flushed on the main thread.");
		}

		for(String i : LOG_ORDER)
		{
			Bukkit.getConsoleSender().sendMessage(LOG_BUFFER.get(i) == 0 ? i : i + (C.GOLD + " +" + LOG_BUFFER.get(i)));
		}

		LOG_BUFFER.clear();
		LOG_ORDER.clear();
	}

	public static void log(String string)
	{
		if(!LOG_BUFFER.containsKey(string))
		{
			LOG_BUFFER.put(string, 0);
			LOG_ORDER.add(string);
		}

		else
		{
			LOG_BUFFER.put(string, LOG_BUFFER.get(string) + 1);
		}
	}

	public static boolean started()
	{
		return started;
	}

	public static void stopAllServices()
	{
		for(IService i : runningServices.v())
		{
			try
			{
				D.as("Phantom > Service Provider").l("Stopping Service " + i.getClass().getSimpleName());
				i.onStop();
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}

		runningServices.clear();
	}

	public static GList<Class<? extends IService>> getRunningServices()
	{
		return runningServices.k();
	}

	public static void stopService(Class<? extends IService> i)
	{
		runningServices.get(i).onStop();
		runningServices.remove(i);
	}

	public static World getDefaultWorld()
	{
		return Bukkit.getWorld("world");
	}

	public static void suckerpunch()
	{
		runningServices.clear();
	}
}

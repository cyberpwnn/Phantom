package com.volmit.phantom.main;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.volmit.phantom.api.job.J;
import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.api.module.ModuleManager;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.util.text.C;

public class Phantom
{
	private static final GMap<String, Integer> LOG_BUFFER = new GMap<String, Integer>();
	private static final GList<String> LOG_ORDER = new GList<String>();
	private static final GMap<Class<? extends IService>, IService> runningServices = new GMap<Class<? extends IService>, IService>();
	private static int lp = 0;
	private static final ModuleManager moduleManager = new ModuleManager();

	public static String tag()
	{
		return C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Phantom" + C.DARK_GRAY + "]" + C.GRAY + ": ";
	}

	public static String tag(String c)
	{
		return C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Phantom" + C.DARK_GRAY + " - " + C.GRAY + c + C.DARK_GRAY + "]" + C.GRAY + ": ";
	}

	public static ModuleManager getModuleManager()
	{
		return moduleManager;
	}

	@SuppressWarnings("unchecked")
	public static <T extends IService> T getService(Class<? extends T> serviceClass)
	{
		try
		{
			if(!runningServices.containsKey(serviceClass))
			{
				IService s = serviceClass.getConstructor().newInstance();
				runningServices.put(serviceClass, s);
				try
				{
					s.onStart();
					D.as("Phantom > Service Provider").l("Started Service: " + serviceClass.getSimpleName());
				}

				catch(Throwable e)
				{
					D.as("Service Provider").w(s.getClass().getSimpleName() + " may have failed to properly start!");
				}
			}
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return (T) runningServices.get(serviceClass);
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

	public static void flushLogBuffer()
	{
		if(!M.interval(Math.max(1, Math.min(58, lp))))
		{
			return;
		}

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
		lp /= 1.25;
	}

	public static void log(String string)
	{
		lp++;

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
		try
		{
			runningServices.get(i).onStop();
		}

		catch(Throwable e)
		{

		}

		runningServices.remove(i);
	}

	public static World getDefaultWorld()
	{
		World w = Bukkit.getWorld("world");

		if(w == null)
		{
			for(World i : Bukkit.getWorlds())
			{
				if(i.getName().contains("/"))
				{
					continue;
				}

				w = i;
				break;
			}
		}

		return w;
	}

	public static void suckerpunch()
	{
		J.ass(() -> J.sr(() -> flushLogBuffer(), 0));
	}
}

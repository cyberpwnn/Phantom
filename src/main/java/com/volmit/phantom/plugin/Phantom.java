package com.volmit.phantom.plugin;

import org.bukkit.Bukkit;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.text.C;

public class Phantom
{
	private static GMap<String, Integer> LOG_BUFFER = new GMap<String, Integer>();
	private static GList<String> LOG_ORDER = new GList<String>();

	public static boolean isMainThread()
	{
		return Bukkit.isPrimaryThread();
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
}

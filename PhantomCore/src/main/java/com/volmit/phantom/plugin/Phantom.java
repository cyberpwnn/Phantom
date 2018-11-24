package com.volmit.phantom.plugin;

import org.bukkit.Bukkit;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.pawn.PawnWorld;

public class Phantom
{
	public static GList<String> LOG_BUFFER = new GList<String>();
	public static PawnWorld PAWN_WORLD = null;
	
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
	}
}

package org.phantomapi;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.phantomapi.core.DMS;
import org.phantomapi.core.PhantomPlugin;
import org.phantomapi.core.Signal;
import phantom.dispatch.PD;
import phantom.pawn.IPawn;
import phantom.pawn.PawnSpace;

public class Phantom
{
	private static final DMS dms = new DMS();
	private static final PawnSpace pawnSpace = new PawnSpace();
	private static PhantomPlugin inst = PhantomPlugin.instance();

	public static void kick(Throwable e)
	{
		e.printStackTrace();
	}

	public static boolean isServerThread()
	{
		return Bukkit.isPrimaryThread();
	}

	public static void touch(PhantomPlugin inst)
	{
		Phantom.inst = inst;
	}

	public static void pulse(Signal signal)
	{
		PD.v("Received pulse: " + signal.toString());
		
		switch(signal)
		{
		case ABORT:
			doAbort();
			break;
		case START:
			doStart();
			break;
		case STOP:
			doStop();
			break;
		default:
			break;
		}
	}

	private static void doStop()
	{
		dms.stop();
	}

	private static void doStart()
	{
		dms.start();
	}

	private static void doAbort()
	{
		try
		{
			doStop();
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	public static void register(Listener listener)
	{
		Bukkit.getPluginManager().registerEvents(listener, inst);
	}

	public static void unregister(Listener listener)
	{
		HandlerList.unregisterAll(listener);
	}

	public static void activate(IPawn pawn)
	{
		pawnSpace.activate(pawn);
	}

	public static void deactivate(IPawn pawn)
	{
		pawnSpace.deactivate(pawn);
	}

	public static void sendConsoleMessage(String message)
	{
		if(!isServerThread())
		{
			return;
		}

		Bukkit.getConsoleSender().sendMessage(message);
	}

	public static String getVersion()
	{
		return inst.getDescription().getVersion();
	}
	
	public static int startTask(int delay, Runnable r)
	{
		return Bukkit.getScheduler().scheduleSyncDelayedTask(inst, r, delay);
	}

	public static int startRepeatingTask(int delay, int interval, Runnable r)
	{
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(inst, r, delay, interval);
	}

	public static void stopTask(int id)
	{
		Bukkit.getScheduler().cancelTask(id);
	}

	public static PawnSpace getPawnSpace()
	{
		return pawnSpace;
	}
}

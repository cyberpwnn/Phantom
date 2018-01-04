package org.phantomapi;

import java.io.File;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginLoader;
import org.phantomapi.service.ClassAnchorService;

import phantom.dispatch.PD;
import phantom.lang.GList;
import phantom.pawn.IPawn;
import phantom.pawn.PawnSpace;
import phantom.service.IService;
import phantom.util.exception.PawnActivationException;
import phantom.util.plugin.PluginUtil;

/**
 * The window of phantom
 *
 * @author cyberpwn
 */
public class Phantom
{
	private static final DMSP dms = new DMSP();
	private static final PawnSpace pawnSpace = new PawnSpace();
	private static PhantomPlugin inst = PhantomPlugin.instance();

	/**
	 * Kick an exception to the console. Allows use for listeners and formatting.
	 *
	 * @param e
	 *            the exception or throwable to kick to the console
	 */
	public static void kick(Throwable e)
	{
		e.printStackTrace();
	}

	/**
	 * Get anchored classes by the given anchor tag name. Used for getting groups of
	 * classes with @Anchor("tag") after the jar is crawled for anchors in classes
	 *
	 * @param tag
	 *            the tag to find in crawled classes
	 * @return a list of classes with the given tag. If no classes are found or
	 *         there is no known tag, an empty list will be returned
	 */
	public static GList<Class<?>> getAnchors(String tag)
	{
		return getService(ClassAnchorService.class).getAnchoredClasses(tag);
	}

	/**
	 * Crawl a jar. Async safe. Phantom is crawled on startup
	 *
	 * @param jar
	 *            the jar to crawl.
	 */
	public static void crawlJar(File jar)
	{
		getService(ClassAnchorService.class).crawl(jar);
	}

	/**
	 * Check if a pawn is activated. Use for singular pawns
	 *
	 * @param pawn
	 *            the pawn to check
	 * @return true if the pawn is active. False if the pawn is inactive, not known,
	 *         does not have an @Name tag, or is not singular
	 */
	public static boolean isActive(IPawn pawn)
	{
		return pawnSpace.isActive(pawn);
	}

	/**
	 * Gets the service from the service manager. If the service is not running, it
	 * will be spun up. If the service does not exist, a null pointer will be
	 * thrown.
	 *
	 * @param svc
	 *            the service class requested
	 * @return the active service of the type requested
	 */
	public static <T extends IService> T getService(Class<? extends T> svc)
	{
		return dms.getApi().getServiceProvider().getService(svc);
	}

	/**
	 * Check if the current thread is the server thread (sync).
	 *
	 * @return returns true if on the main thread, false if otherwise
	 */
	public static boolean isServerThread()
	{
		return Bukkit.isPrimaryThread();
	}

	/**
	 * Internal use for setting up phantom on startup. Calling this is not wise.
	 *
	 * @param inst
	 *            the phantom instance
	 */
	public static void touch(PhantomPlugin inst)
	{
		Phantom.inst = inst;
	}

	/**
	 * Pulse a signal to phantom (internal use)
	 *
	 * @param signal
	 *            the signal to pulse to DMSp
	 */
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

	/**
	 * Register a bukkit event listener
	 *
	 * @param listener
	 *            the listener to register
	 */
	public static void register(Listener listener)
	{
		Bukkit.getPluginManager().registerEvents(listener, inst);
	}

	/**
	 * Unregister a bukkit event listener
	 *
	 * @param listener
	 *            the listener to unregister
	 */
	public static void unregister(Listener listener)
	{
		HandlerList.unregisterAll(listener);
	}

	/**
	 * Force claim a pawn as your own subpawn. You need a field which will hold the
	 * pawn, and it is trusted that you place the given (claimed) pawn into the
	 * given field pawn before calling this.
	 *
	 * @param owner
	 *            the owner pawn (super)
	 * @param claimed
	 *            the claimed pawn (sub)
	 * @param field
	 *            the field in which the claimed pawn (sub) is placed which the
	 *            field should reside in the claiming owner pawn (super)
	 */
	public static void claim(IPawn owner, IPawn claimed, Field field)
	{
		pawnSpace.claim(owner, field, claimed);
	}

	/**
	 * Activate a pawn
	 *
	 * @param pawn
	 *            the pawn to activate. If a pawn is singular and it is activated
	 *            but another pawn of the same type is already activated, an
	 *            exception will be kicked to the console
	 */
	public static void activate(IPawn pawn)
	{
		try
		{
			pawnSpace.activate(pawn);
		}

		catch(PawnActivationException e)
		{
			kick(e);
		}
	}

	/**
	 * Deactivate a pawn
	 *
	 * @param pawn
	 *            the pawn to shut down
	 */
	public static void deactivate(IPawn pawn)
	{
		pawnSpace.deactivate(pawn);
	}

	/**
	 * Send a console message. The message will not be sent if called off of the
	 * main thread
	 *
	 * @param message
	 *            the message to send.
	 */
	public static void sendConsoleMessage(String message)
	{
		if(!isServerThread())
		{
			return;
		}

		Bukkit.getConsoleSender().sendMessage(message);
	}

	/**
	 * Get the version of phantom
	 *
	 * @return phantom's version
	 */
	public static String getVersion()
	{
		return inst.getDescription().getVersion();
	}

	/**
	 * Start a bukkit scheduled task
	 *
	 * @param delay
	 *            the delay before running
	 * @param r
	 *            the runnable to run
	 * @return the task id
	 */
	public static int startTask(int delay, Runnable r)
	{
		return Bukkit.getScheduler().scheduleSyncDelayedTask(inst, r, delay);
	}

	/**
	 * Start a repeating task
	 *
	 * @param delay
	 *            the delay before running
	 * @param interval
	 *            the interval between runs (0 is every tick)
	 * @param r
	 *            the runnable to run
	 * @return the task id
	 */
	public static int startRepeatingTask(int delay, int interval, Runnable r)
	{
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(inst, r, delay, interval);
	}

	/**
	 * Stop a task
	 *
	 * @param id
	 *            the task to stop by id
	 */
	public static void stopTask(int id)
	{
		Bukkit.getScheduler().cancelTask(id);
	}

	/**
	 * Get the pawn space for managing pawns
	 *
	 * @return the pawn space
	 */
	public static PawnSpace getPawnSpace()
	{
		return pawnSpace;
	}

	/**
	 * Get phantoms data folder
	 *
	 * @return the data folder
	 */
	public static File getDataFolder()
	{
		return inst.getDataFolder();
	}

	/**
	 * Get phantoms plugin loader
	 *
	 * @return the plugin loader for phantom
	 */
	public static PluginLoader getPluginLoader()
	{
		return inst.getPluginLoader();
	}

	/**
	 * Get phantoms plugin jar file
	 *
	 * @return the jar file of phantom
	 */
	public static File getPluginFile()
	{
		return PluginUtil.getPluginFile(inst);
	}
}

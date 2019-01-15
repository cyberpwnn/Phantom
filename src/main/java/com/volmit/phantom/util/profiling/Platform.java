package com.volmit.phantom.util.profiling;

import java.io.File;

//TODO WHERE MY REACT GONE
import com.volmit.phantom.api.math.M;

/**
 * Server platform tools
 *
 * @author cyberpwn
 *
 */
public class Platform
{
	/**
	 * Get the time when the server started up (uses server.properties modification
	 * date)
	 *
	 * @return the time when the server fully started up.
	 */
	public static long getStartupTime()
	{
		return new File("server.properties").lastModified();
	}

	/**
	 * Returns the time the server has been online. Reloading does not reset this.
	 *
	 * @return the ACTAUL server uptime
	 */
	public static long getUpTime()
	{
		return M.ms() - getStartupTime();
	}

	public static boolean isBukkit()
	{
		try
		{
			Class.forName("org.bukkit.Bukkit");
			return true;
		}

		catch(Throwable e)
		{
		}
		return false;
	}

	public static boolean isBungeecord()
	{
		try
		{
			Class.forName("net.md_5.bungee.api.plugin.Plugin");
			return true;
		}

		catch(Throwable e)
		{

		}
		return false;
	}
}

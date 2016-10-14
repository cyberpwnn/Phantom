package org.phantomapi.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;

/**
 * Dispatcher
 * 
 * @author cyberpwn
 */
public class D
{
	public static boolean fool = false;
	private String name;
	public static GList<String> queue;
	
	/**
	 * Create a dispatcher
	 * 
	 * @param name
	 *            the tag or prefix for logged messages for this dispatcher
	 */
	public D(String name)
	{
		this.name = name;
	}
	
	public enum DispatchType
	{
		INFO, SUCCESS, FAILURE, WARNING, VERBOSE, OVERBOSE
	}
	
	protected static Boolean silent = false;
	
	private void log(DispatchType type, String s, String... o)
	{
		if(silent || Phantom.isSilenced(this))
		{
			return;
		}
		
		String msg = "";
		
		for(String i : o)
		{
			msg = msg + i;
		}
		
		msg = s + msg;
		
		String tmg = C.getLastColors(msg) + "|" + C.DARK_GRAY + name + ": " + ChatColor.WHITE + msg;
		
		if(fool)
		{
			tmg = StringUtils.repeat(" ", (int) (8 * Math.random())) + ChatColor.WHITE + C.getLastColors(msg) + StringUtils.reverse(C.stripColor(msg)) + ": " + C.DARK_GRAY + StringUtils.reverse(name)  + C.getLastColors(msg) + "|";
		}
		
		if(Phantom.isAsync())
		{
			tmg = C.getLastColors(msg) + "|" + "ASYNC" + tmg;
			queue.add(tmg);
		}
		
		else
		{
			flush();
			Bukkit.getServer().getConsoleSender().sendMessage(tmg);
		}
	}
	
	public static void flush()
	{
		if(!queue.isEmpty())
		{
			try
			{
				for(String i : queue.copy())
				{
					try
					{
						Bukkit.getServer().getConsoleSender().sendMessage(i);
					}
					
					catch(Exception e)
					{
						
					}
				}
			}
			
			catch(Exception e)
			{
				return;
			}
			
			queue.clear();
		}
	}
	
	/**
	 * Message info (white)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void info(String... o)
	{
		log(DispatchType.INFO, "" + ChatColor.WHITE, o);
	}
	
	/**
	 * Message info (white)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void i(String... s)
	{
		info(s);
	}
	
	/**
	 * Message success (green)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void success(String... o)
	{
		log(DispatchType.SUCCESS, "" + ChatColor.GREEN, o);
	}
	
	/**
	 * Message success (green)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void s(String... o)
	{
		success(o);
	}
	
	/**
	 * Message failure (red)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void failure(String... o)
	{
		log(DispatchType.FAILURE, "" + ChatColor.RED, o);
	}
	
	/**
	 * Message failure (red)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void f(String... o)
	{
		failure(o);
	}
	
	/**
	 * Message warning (yellow)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void warning(String... o)
	{
		log(DispatchType.WARNING, "" + ChatColor.YELLOW, o);
	}
	
	/**
	 * Message warning (yellow)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void w(String... o)
	{
		warning(o);
	}
	
	/**
	 * Message verbose (light purple)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void verbose(String... o)
	{
		log(DispatchType.VERBOSE, "" + ChatColor.LIGHT_PURPLE, o);
	}
	
	/**
	 * Message verbose (light purple)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void v(String... o)
	{
		verbose(o);
	}
	
	/**
	 * Message overbose (aqua)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void overbose(String... o)
	{
		log(DispatchType.OVERBOSE, "" + ChatColor.AQUA, o);
	}
	
	/**
	 * Message overbose (aqua)
	 * 
	 * @param o
	 *            strings/string
	 */
	public void o(String... o)
	{
		overbose(o);
	}
	
	/**
	 * Get the name
	 * 
	 * @return the name of this dispatcher tag
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Set the name
	 * 
	 * @param name
	 *            the name of this dispatcher tag
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Is this dispatcher silent?
	 * 
	 * @return true if silenced
	 */
	public Boolean isSilent()
	{
		return silent;
	}
	
	/**
	 * Set the silence value of this dispatcher
	 * 
	 * @param silent
	 *            sincence
	 */
	public void setSilent(Boolean silent)
	{
		D.silent = silent;
	}
	
	static
	{
		queue = new GList<String>();
	}
}

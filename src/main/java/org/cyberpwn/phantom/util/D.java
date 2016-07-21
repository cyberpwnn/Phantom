package org.cyberpwn.phantom.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Dispatcher
 * 
 * @author cyberpwn
 *
 */
public class D
{
	private String name;
	
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
		if(silent)
		{
			return;
		}
		
		String msg = s + "";
		
		for(String i : o)
		{
			msg = msg + i;
		}
		
		String tmg = ChatColor.AQUA + type.toString() + ": " + ChatColor.GOLD + "/" + name + ": " + ChatColor.WHITE + msg;
		Bukkit.getServer().getConsoleSender().sendMessage(tmg);
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
}

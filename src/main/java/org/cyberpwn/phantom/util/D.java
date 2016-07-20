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
	
	public void info(String... o)
	{
		log(DispatchType.INFO, "" + ChatColor.WHITE, o);
	}
	
	public void i(String... s)
	{
		info(s);
	}
	
	public void success(String... o)
	{
		log(DispatchType.SUCCESS, "" + ChatColor.GREEN, o);
	}
	
	public void s(String... o)
	{
		success(o);
	}
	
	public void failure(String... o)
	{
		log(DispatchType.FAILURE, "" + ChatColor.RED, o);
	}
	
	public void f(String... o)
	{
		failure(o);
	}
	
	public void warning(String... o)
	{
		log(DispatchType.WARNING, "" + ChatColor.YELLOW, o);
	}
	
	public void w(String... o)
	{
		warning(o);
	}
	
	public void verbose(String... o)
	{
		log(DispatchType.VERBOSE, "" + ChatColor.LIGHT_PURPLE, o);
	}
	
	public void v(String... o)
	{
		verbose(o);
	}
	
	public void overbose(String... o)
	{
		log(DispatchType.OVERBOSE, "" + ChatColor.AQUA, o);
	}
	
	public void o(String... o)
	{
		overbose(o);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Boolean getSilent()
	{
		return silent;
	}
	
	public void setSilent(Boolean silent)
	{
		D.silent = silent;
	}
}

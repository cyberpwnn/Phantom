package org.phantomapi.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.sync.S;

/**
 * Dispatcher
 * 
 * @author cyberpwn
 */
public class D
{
	public static boolean rdebug = false;
	public static boolean fool = false;
	private String name;
	private GList<Player> listeners;
	public static GList<String> queue;
	public static GList<Player> globalListeners;
	
	public static void d(Object inst, String s)
	{
		if(!rdebug)
		{
			return;
		}
		
		try
		{
			if(Phantom.isAsync())
			{
				new S()
				{
					@Override
					public void sync()
					{
						d(inst, s + C.WHITE + " (ASYNC)");
					}
				};
				
				return;
			}
		}
		
		catch(Exception e)
		{
			
		}
		
		d(inst.getClass(), s);
	}
	
	public static void d(Class<?> inst, String s)
	{
		if(rdebug)
		{
			Bukkit.getServer().getConsoleSender().sendMessage(C.RED + "DEBUG<" + C.WHITE + inst.getSimpleName() + C.RED + ">" + C.WHITE + s);
		}
	}
	
	/**
	 * Create a dispatcher
	 * 
	 * @param name
	 *            the tag or prefix for logged messages for this dispatcher
	 */
	public D(String name)
	{
		this.name = name;
		listeners = new GList<Player>();
	}
	
	public enum DispatchType
	{
		INFO,
		SUCCESS,
		FAILURE,
		WARNING,
		VERBOSE,
		OVERBOSE
	}
	
	protected static Boolean silent = false;
	
	/**
	 * Get listeners
	 * 
	 * @return the players listening on this dispatcher
	 */
	public GList<Player> getListeners()
	{
		return listeners;
	}
	
	/**
	 * Get the global listeners
	 * 
	 * @return the global listeners
	 */
	public static GList<Player> getGlobalListeners()
	{
		return globalListeners;
	}
	
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
			tmg = StringUtils.repeat(" ", (int) (8 * Math.random())) + ChatColor.WHITE + C.getLastColors(msg) + StringUtils.reverse(C.stripColor(msg)) + ": " + C.DARK_GRAY + StringUtils.reverse(name) + C.getLastColors(msg) + "|";
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
			
			for(Player i : listeners)
			{
				i.sendMessage(tmg);
			}
			
			for(Player i : globalListeners)
			{
				i.sendMessage(tmg);
			}
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
						
						for(Player j : globalListeners)
						{
							j.sendMessage(i);
						}
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
		globalListeners = new GList<Player>();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		D other = (D) obj;
		
		if(name == null)
		{
			if(other.name != null)
			{
				return false;
			}
		}
		
		else if(!name.equals(other.name))
		{
			return false;
		}
		
		return true;
	}
}

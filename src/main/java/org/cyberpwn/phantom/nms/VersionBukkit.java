package org.cyberpwn.phantom.nms;

import org.bukkit.Bukkit;

/**
 * Versioning utils
 * 
 * @author cyberpwn
 *
 */
public enum VersionBukkit
{
	VU, V7, V8, V9, V11;
	
	/**
	 * Basically, is it not 1.8, and below 1.8
	 * 
	 * @return
	 */
	public static boolean tc()
	{
		if(get().equals(VU) || get().equals(V7))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Get the version
	 * 
	 * @return the version
	 */
	public static VersionBukkit get()
	{
		if(Bukkit.getBukkitVersion().startsWith("1.7"))
		{
			return V7;
		}
		
		if(Bukkit.getBukkitVersion().startsWith("1.8"))
		{
			return V8;
		}
		
		if(Bukkit.getBukkitVersion().startsWith("1.9"))
		{
			return V9;
		}
		
		if(Bukkit.getBukkitVersion().startsWith("1.10"))
		{
			return V11;
		}
		
		return VU;
	}
}

package org.phantomapi.util;

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.stack.Stack;
import com.google.common.base.Charsets;

/**
 * Object conversion (to string and from string)
 * 
 * @author cyberpwn
 */
public class OC
{
	public static final String C = ":";
	
	/**
	 * Convert an itemstack to a string
	 * 
	 * @param is
	 *            the itemstack
	 * @return the byted string
	 */
	public static String fromItemStack(ItemStack is)
	{
		try
		{
			return new String(new Stack(is).toData(), Charsets.UTF_8);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Convert a string to an itemstack
	 * 
	 * @param data
	 *            the byted string
	 * @return the itemStack
	 */
	public ItemStack toItemStack(String data)
	{
		Stack s = new Stack(Material.AIR);
		
		try
		{
			s.fromData(data.getBytes());
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return s.toItemStack();
	}
	
	/**
	 * Convert a location to a string
	 * 
	 * @param l
	 *            the location
	 * @return the string
	 */
	public static String fromLocation(Location l)
	{
		return fromWorld(l.getWorld()) + C + l.getX() + C + l.getY() + C + l.getZ() + C + l.getYaw() + C + l.getPitch();
	}
	
	/**
	 * Convert a string to a location
	 * 
	 * @param l
	 *            the string
	 * @return the location
	 */
	public static Location toLocation(String l)
	{
		return new Location(toWorld(s(l, 0)), toD(s(l, 1)), toD(s(l, 2)), toD(s(l, 3)), toD(s(l, 4)).floatValue(), toD(s(l, 5)).floatValue());
	}
	
	/**
	 * Convert a world to a string
	 * 
	 * @param w
	 *            the world
	 * @return the string
	 */
	public static String fromWorld(World w)
	{
		return w.getName();
	}
	
	/**
	 * Convert a string to a world
	 * 
	 * @param w
	 *            the string
	 * @return the world
	 */
	public static World toWorld(String w)
	{
		return Bukkit.getWorld(w);
	}
	
	/**
	 * Double from string
	 * 
	 * @param d
	 *            string
	 * @return double
	 */
	public static Double toD(String d)
	{
		return Double.valueOf(d);
	}
	
	/**
	 * Double to string
	 * 
	 * @param d
	 *            the double
	 * @return the string
	 */
	public static String fromD(Double d)
	{
		return String.valueOf(d);
	}
	
	/**
	 * Select a slot from a split string
	 * 
	 * @param s
	 *            the string
	 * @param i
	 *            the slot
	 * @return the selected string split by OC.C
	 */
	public static String s(String s, int i)
	{
		return s.split(C)[i];
	}
}

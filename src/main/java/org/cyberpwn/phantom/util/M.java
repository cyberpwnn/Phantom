package org.cyberpwn.phantom.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.world.Area;

/**
 * Math
 * 
 * @author cyberpwn
 *
 */
public class M
{
	/**
	 * Get the ticks per second from a time in nanoseconds, the rad can be used
	 * for multiple ticks
	 * 
	 * @param ns
	 *            the time in nanoseconds
	 * @param rad
	 *            the radius of the time
	 * @return the ticks per second in double form
	 */
	public static double tps(long ns, int rad)
	{
		return (20.0 * (ns / 50000000.0)) / rad;
	}
	
	/**
	 * Get the number of ticks from a time in nanoseconds
	 * 
	 * @param ns
	 *            the nanoseconds
	 * @return the amount of ticks
	 */
	public static double ticksFromNS(long ns)
	{
		return (ns / 50000000.0);
	}
	
	/**
	 * Get system Nanoseconds
	 * 
	 * @return nanoseconds (current)
	 */
	public static long ns()
	{
		return System.nanoTime();
	}
	
	/**
	 * Get the current millisecond time
	 * 
	 * @return milliseconds
	 */
	public static long ms()
	{
		return System.currentTimeMillis();
	}
	
	/**
	 * Average a list of doubles
	 * 
	 * @param doubles
	 *            the doubles
	 * @return the average
	 */
	public static double avg(GList<Double> doubles)
	{
		double a = 0.0;
		
		for(double i : doubles)
		{
			a += i;
		}
		
		return a / doubles.size();
	}
	
	/**
	 * Cull a list of doubles
	 * 
	 * @param doubles
	 *            the doubles
	 * @param limit
	 *            the limit size
	 */
	public static void lim(GList<Double> doubles, int limit)
	{
		while(doubles.size() > limit)
		{
			doubles.remove(0);
		}
	}
	
	/**
	 * Get a speed value from a vector (velocity)
	 * 
	 * @param v
	 *            the vector
	 * @return the speed
	 */
	public static double getSpeed(Vector v)
	{
		Vector vi = new Vector(0, 0, 0);
		Vector vt = new Vector(0, 0, 0).add(v);
		
		return vi.distance(vt);
	}
	
	/**
	 * An alternative method of distance calculation
	 * 
	 * @param a
	 *            the first location
	 * @param b
	 *            the second
	 * @return the distance between the two
	 */
	public static double distance(Location a, Location b)
	{
		return Double.longBitsToDouble(((Double.doubleToLongBits(a.distanceSquared(b)) - (1l << 52)) >> 1) + (1l << 61));
	}
	
	/**
	 * Check if a location is within a given range of another without using sqrt
	 * functions
	 * 
	 * @param center
	 *            the center (first position)
	 * @param check
	 *            the check location (second position)
	 * @param radius
	 *            the radius to check
	 * @return true if the check is within the given range of the center
	 *         position
	 */
	public static boolean within(Location center, Location check, Double radius)
	{
		return Area.within(center, check, radius);
	}
}

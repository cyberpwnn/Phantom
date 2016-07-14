package org.cyberpwn.phantom.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.world.Area;

public class M
{
	public static double tps(long ns, int rad)
	{
		return (20.0 * (ns / 50000000.0)) / rad;
	}
	
	public static int ticksFromNS(long ns)
	{
		return (int) (ns / 50000000.0);
	}
	
	public static long ns()
	{
		return System.nanoTime();
	}
	
	public static long ms()
	{
		return System.currentTimeMillis();
	}
	
	public static double avg(GList<Double> doubles)
	{
		double a = 0.0;
		
		for(double i : doubles)
		{
			a += i;
		}
		
		return a / doubles.size();
	}
	
	public static void lim(GList<Double> doubles, int limit)
	{
		while(doubles.size() > limit)
		{
			doubles.remove(0);
		}
	}
	
	public static double getSpeed(Vector v)
	{
		Vector vi = new Vector(0, 0, 0);
		Vector vt = new Vector(0, 0, 0).add(v);
		
		return vi.distance(vt);
	}
	
	public static double distance(Location a, Location b)
	{
		return Double.longBitsToDouble(((Double.doubleToLongBits(a.distanceSquared(b)) - (1l << 52)) >> 1) + (1l << 61));
	}
	
	public static boolean within(Location center, Location check, Double radius)
	{
		return Area.within(center, check, radius);
	}
}

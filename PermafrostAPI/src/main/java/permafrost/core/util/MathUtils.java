package permafrost.core.util;

import org.bukkit.Location;

public class MathUtils
{
	public static double percent(int i, int of)
	{
		return ((double) i) / ((double) of);
	}
	
	public static double percent(double from, double to, double of)
	{
		return ((double) Math.abs(from - to)) / ((double) Math.abs(from - of));
	}
	
	public static double percent(int from, int to, int of)
	{
		return percent((double) from, (double) to, (double) of);
	}
	
	public static boolean isWithin(Location a, Location b, double rad)
	{
		return distanceSquared(a, b) <= rad * rad;
	}
	
	public static double distanceSquared2D(Location a, Location b)
	{
		return Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2);
	}
	
	public static double distance2D(Location a, Location b)
	{
		return Math.sqrt(distanceSquared2D(a, b));
	}
	
	public static double distance(Location a, Location b)
	{
		return a.distance(b);
	}
	
	public static double distanceSquared(Location a, Location b)
	{
		return a.distanceSquared(b);
	}
}

package org.cyberpwn.phantom.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.Location;
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
	 * Evaluates an expression using javascript engine and returns the double
	 * 
	 * @param expression
	 *            the mathimatical expression
	 * @return the double result
	 * @throws ScriptException
	 *             dont fuck up.
	 */
	public static double evaluate(String expression) throws ScriptException
	{
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine scriptEngine = mgr.getEngineByName("JavaScript");
		
		return Double.valueOf(scriptEngine.eval(expression).toString());
	}
	
	/**
	 * Evaluates an expression using javascript engine and returns the double
	 * result. This can take variable parameters, so you need to define them.
	 * 
	 * Parameters are defined as $[0-9]. For example evaluate("4$0/$1", 1, 2);
	 * This makes the expression (4x1)/2 == 2. Keep note that you must use 0-9,
	 * you cannot skip, or start at a number other than 0.
	 * 
	 * @param expression
	 *            the expression with variables
	 * @param args
	 *            the arguments/variables
	 * @return the resulting double value
	 * @throws ScriptException
	 *             dont fuck up
	 * @throws IndexOutOfBoundsException
	 *             learn to count
	 */
	public static double evaluate(String expression, Double... args) throws ScriptException, IndexOutOfBoundsException
	{
		for(int i = 0; i < args.length; i++)
		{
			String current = "$" + i;
			
			if(expression.contains(current))
			{
				expression.replaceAll(current, args[i] + "");
			}
		}
		
		return evaluate(expression);
	}
	
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

package org.phantomapi.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.bukkit.Location;
import org.phantomapi.lang.GList;
import org.phantomapi.world.Area;

/**
 * Math
 * 
 * @author cyberpwn
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
				expression = expression.replaceAll(Matcher.quoteReplacement(current), args[i] + "");
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
	 * Get roman numeral representation of the int
	 * 
	 * @param num
	 *            the int
	 * @return the numerals
	 */
	public static String toRoman(int num)
	{
		LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
		
		roman_numerals.put("M", 1000);
		roman_numerals.put("CM", 900);
		roman_numerals.put("D", 500);
		roman_numerals.put("CD", 400);
		roman_numerals.put("C", 100);
		roman_numerals.put("XC", 90);
		roman_numerals.put("L", 50);
		roman_numerals.put("XL", 40);
		roman_numerals.put("X", 10);
		roman_numerals.put("IX", 9);
		roman_numerals.put("V", 5);
		roman_numerals.put("IV", 4);
		roman_numerals.put("I", 1);
		
		String res = "";
		
		for(Map.Entry<String, Integer> entry : roman_numerals.entrySet())
		{
			int matches = num / entry.getValue();
			
			res += repeat(entry.getKey(), matches);
			num = num % entry.getValue();
		}
		
		return res;
	}
	
	private static String repeat(String s, int n)
	{
		if(s == null)
		{
			return null;
		}
		
		final StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < n; i++)
		{
			sb.append(s);
		}
		
		return sb.toString();
	}
	
	/**
	 * Get the number representation from roman numerals.
	 * 
	 * @param number
	 *            the roman number
	 * @return the int representation
	 */
	public static int fromRoman(String number)
	{
		if(number.isEmpty())
		{
			return 0;
		}
		
		number = number.toUpperCase();
		
		if(number.startsWith("M"))
		{
			return 1000 + fromRoman(number.substring(1));
		}
		
		if(number.startsWith("CM"))
		{
			return 900 + fromRoman(number.substring(2));
		}
		
		if(number.startsWith("D"))
		{
			return 500 + fromRoman(number.substring(1));
		}
		
		if(number.startsWith("CD"))
		{
			return 400 + fromRoman(number.substring(2));
		}
		
		if(number.startsWith("C"))
		{
			return 100 + fromRoman(number.substring(1));
		}
		
		if(number.startsWith("XC"))
		{
			return 90 + fromRoman(number.substring(2));
		}
		
		if(number.startsWith("L"))
		{
			return 50 + fromRoman(number.substring(1));
		}
		
		if(number.startsWith("XL"))
		{
			return 40 + fromRoman(number.substring(2));
		}
		
		if(number.startsWith("X"))
		{
			return 10 + fromRoman(number.substring(1));
		}
		
		if(number.startsWith("IX"))
		{
			return 9 + fromRoman(number.substring(2));
		}
		
		if(number.startsWith("V"))
		{
			return 5 + fromRoman(number.substring(1));
		}
		
		if(number.startsWith("IV"))
		{
			return 4 + fromRoman(number.substring(2));
		}
		
		if(number.startsWith("I"))
		{
			return 1 + fromRoman(number.substring(1));
		}
		
		return 0;
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
	
	/**
	 * Biggest number
	 * 
	 * @param ints
	 *            the numbers
	 * @return the biggest one
	 */
	public static int max(int... ints)
	{
		int max = Integer.MIN_VALUE;
		
		for(int i : ints)
		{
			if(i > max)
			{
				max = i;
			}
		}
		
		return max;
	}
	
	/**
	 * Smallest number
	 * 
	 * @param ints
	 *            the numbers
	 * @return the smallest one
	 */
	public static int min(int... ints)
	{
		int min = Integer.MAX_VALUE;
		
		for(int i : ints)
		{
			if(i < min)
			{
				min = i;
			}
		}
		
		return min;
	}
	
	/**
	 * is the number "is" within from-to
	 * 
	 * @param from
	 *            the lower end
	 * @param to
	 *            the upper end
	 * @param is
	 *            the check
	 * @return true if its within
	 */
	public static boolean within(int from, int to, int is)
	{
		return is >= from && is <= to;
	}
}

package org.cyberpwn.phantom.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formatter
 * 
 * @author cyberpwn
 *
 */
public class F
{
	private static NumberFormat NF;
	private static DecimalFormat DF;
	
	private static void instantiate()
	{
		if(NF == null)
		{
			NF = NumberFormat.getInstance(Locale.US);
		}
	}
	
	public static String color(String msg)
	{
		String coloredMsg = "";
		
		for(int i = 0; i < msg.length(); i++)
		{
			if(msg.charAt(i) == '&')
			{
				coloredMsg += '\u00A7';
			}
			
			else
			{
				coloredMsg += msg.charAt(i);
			}
		}
		
		return coloredMsg;
	}
	
	public static String trim(String s, int l)
	{
		if(s.length() <= l)
		{
			return s;
		}
		
		return s.substring(0, l) + "...";
	}
	
	public static String cname(String clazz)
	{
		String codeName = "";
		
		for(Character i : clazz.toCharArray())
		{
			if(Character.isUpperCase(i))
			{
				codeName = codeName + "-" + Character.toLowerCase(i);
			}
			
			else
			{
				codeName = codeName + i;
			}
		}
		
		if(codeName.startsWith("-"))
		{
			codeName = codeName.substring(1);
		}
		
		return codeName;
	}
	
	public static String mem(long mb)
	{
		if(mb < 1024)
		{
			return f(mb) + " MB";
		}
		
		else
		{
			return f(((double) mb / (double) 1024), 1) + " GB";
		}
	}
	
	public static String memx(long kb)
	{
		if(kb < 1024)
		{
			return fd(kb, 2) + " KB";
		}
		
		else
		{
			double mb = (double) kb / 1024.0;
			
			if(mb < 1024)
			{
				return fd(mb, 2) + " MB";
			}
			
			else
			{
				double gb = (double) mb / 1024.0;
				
				return fd(gb, 2) + " GB";
			}
		}
	}
	
	public static String f(long i)
	{
		instantiate();
		return NF.format(i);
	}
	
	public static String f(int i)
	{
		instantiate();
		return NF.format(i);
	}
	
	public static String repeat(String s, int p)
	{
		String k = "";
		
		for(int i = 0; i < p; i++)
		{
			k = k + s;
		}
		
		return k;
	}
	
	public static String f(double i, int p)
	{
		String form = "#";
		
		if(p > 0)
		{
			form = form + "." + repeat("#", p);
		}
		
		DF = new DecimalFormat(form);
		
		return DF.format(i);
	}
	
	public static String fd(double i, int p)
	{
		String form = "0";
		
		if(p > 0)
		{
			form = form + "." + repeat("0", p);
		}
		
		DF = new DecimalFormat(form);
		
		return DF.format(i);
	}
	
	public static String f(float i, int p)
	{
		String form = "#";
		
		if(p > 0)
		{
			form = form + "." + repeat("#", p);
		}
		
		DF = new DecimalFormat(form);
		
		return DF.format(i);
	}
	
	public static String f(double i)
	{
		return f(i, 1);
	}
	
	public static String f(float i)
	{
		return f(i, 1);
	}
	
	public static String pc(double i, int p)
	{
		return f(i * 100.0, p) + "%";
	}
	
	public static String pc(float i, int p)
	{
		return f(i * 100, p) + "%";
	}
	
	public static String pc(double i)
	{
		return f(i * 100, 0) + "%";
	}
	
	public static String pc(float i)
	{
		return f(i * 100, 0) + "%";
	}
	
	public static String pc(int i, int of, int p)
	{
		return f(100.0 * (((double) i) / ((double) of)), p) + "%";
	}
	
	public static String pc(int i, int of)
	{
		return pc(i, of, 0);
	}
	
	public static String pc(long i, long of, int p)
	{
		return f(100.0 * (((double) i) / ((double) of)), p) + "%";
	}
	
	public static String pc(long i, long of)
	{
		return pc(i, of, 0);
	}
	
	public static String msSeconds(long ms)
	{
		return f((double) ms / 1000.0);
	}
	
	public static String msSeconds(long ms, int p)
	{
		return f((double) ms / 1000.0, p);
	}
	
	public static String nsMs(long ns)
	{
		return f((double) ns / 1000000.0);
	}
	
	public static String nsMs(long ns, int p)
	{
		return f((double) ns / 1000000.0, p);
	}
}

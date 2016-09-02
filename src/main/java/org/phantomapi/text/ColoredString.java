package org.phantomapi.text;

import org.phantomapi.util.C;

/**
 * Represents a colored String
 * 
 * @author cyberpwn
 */
public class ColoredString
{
	private C c;
	private String s;
	
	/**
	 * Create a colored string
	 * 
	 * @param c
	 *            the color
	 * @param s
	 *            the string
	 */
	public ColoredString(C c, String s)
	{
		this.c = c;
		this.s = s;
	}
	
	public String toString()
	{
		return c.toString() + s;
	}
	
	public C getC()
	{
		return c;
	}
	
	public void setC(C c)
	{
		this.c = c;
	}
	
	public String getS()
	{
		return s;
	}
	
	public void setS(String s)
	{
		this.s = s;
	}
}

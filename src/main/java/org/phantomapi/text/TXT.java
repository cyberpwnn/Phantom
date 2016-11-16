package org.phantomapi.text;

import org.phantomapi.util.C;
import org.phantomapi.util.F;

/**
 * Textual Utilities
 * 
 * @author cyberpwn
 */
public class TXT
{
	/**
	 * Repeat a string
	 * 
	 * @param str
	 *            the string
	 * @param len
	 *            the amount of repeats
	 * @return the string
	 */
	public static String repeat(String str, int len)
	{
		return F.repeat(str, len);
	}
	
	/**
	 * Create a line
	 * 
	 * @param color
	 *            the color
	 * @param len
	 *            the length
	 * @return the line
	 */
	public static String line(C color, int len)
	{
		return color + "" + C.STRIKETHROUGH + repeat(" ", len);
	}
	
	/**
	 * Create an underline
	 * 
	 * @param color
	 *            the color
	 * @param len
	 *            the length
	 * @return the line
	 */
	public static String underline(C color, int len)
	{
		return color + "" + C.STRIKETHROUGH + repeat(" ", len);
	}
}

package org.phantomapi.text;

/**
 * A progress spinner. Calling toString will not only return the next state, but
 * it will also set the next index
 * 
 * @author cyberpwn
 */
public class ProgressSpinner
{
	private String[] chars;
	private int index;
	
	/**
	 * Create a custom spinner
	 * 
	 * @param chars
	 *            the animation chars
	 */
	public ProgressSpinner(String... chars)
	{
		this.index = 0;
		this.chars = chars;
	}
	
	/**
	 * Create a default spinner
	 */
	public ProgressSpinner()
	{
		this("" + '\u25d0', "" + '\u25d3', "" + '\u25d4', "" + '\u25d1', "" + '\u25d5');
	}
	
	/**
	 * Get the next char from the index
	 */
	public String toString()
	{
		if(chars.length > index + 1)
		{
			index++;
		}
		
		else
		{
			index = 0;
		}
		
		return chars[index] + "";
	}
}

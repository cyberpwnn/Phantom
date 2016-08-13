package org.phantomapi.gui;

import org.phantomapi.lang.GMap;

/**
 * A Window Border info holder
 * 
 * @author cyberpwn
 */
public class WindowBorder
{
	private GMap<BorderDirection, Integer> directionSet;
	
	/**
	 * Make a window border
	 */
	public WindowBorder()
	{
		directionSet = new GMap<BorderDirection, Integer>();
		put(BorderDirection.TOP, 0);
		put(BorderDirection.BOTTOM, 0);
		put(BorderDirection.LEFT, 0);
		put(BorderDirection.RIGHT, 0);
	}
	
	/**
	 * Set a border
	 * 
	 * @param d
	 *            the face
	 * @param p
	 *            the amount
	 * @return this
	 */
	public WindowBorder put(BorderDirection d, int p)
	{
		directionSet.put(d, p);
		
		return this;
	}
	
	/**
	 * Check if a slot is valid placement for this border
	 * 
	 * @param s
	 *            the slot
	 * @return true if its valid (INSIDE the border)
	 */
	public boolean isValid(Slot s)
	{
		if(s.getY() <= directionSet.get(BorderDirection.TOP))
		{
			return false;
		}
		
		if(s.getY() > 6 - directionSet.get(BorderDirection.BOTTOM))
		{
			return false;
		}
		
		if(s.getX() + 4 < directionSet.get(BorderDirection.LEFT))
		{
			return false;
		}
		
		if(s.getX() - 4 < directionSet.get(BorderDirection.RIGHT))
		{
			return false;
		}
		
		return true;
	}
}

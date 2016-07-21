package org.cyberpwn.phantom.gui;

/**
 * A Slot instance for holding an item slot number
 * 
 * @author cyberpwn
 *
 */
public class Slot
{
	protected Integer x;
	protected Integer y;
	protected Integer s;
	
	/**
	 * Create an item slot based on x and y coordinates
	 * 
	 * @param x
	 *            must be between -4 and 4, 0 being the center. -4 is the far
	 *            left
	 * @param y
	 *            must be between 1 and 6. 1 being the top, and 6 the far bottom
	 */
	public Slot(Integer x, Integer y)
	{
		if(x > 4)
		{
			x = 4;
		}
		
		if(x < -4)
		{
			x = -4;
		}
		
		if(y < 1)
		{
			y = 1;
		}
		
		this.x = x;
		this.y = y;
		this.s = getPosition(x, y);
	}
	
	/**
	 * Create a slot based on the actual slot number
	 * 
	 * @param s
	 *            the slot
	 */
	public Slot(Integer s)
	{
		this.s = s;
		this.y = (int) ((s / 9) + 1);
		this.x = (s - ((y - 1) * 9)) - 4;
	}
	
	/**
	 * Get the raw slot number
	 * 
	 * @return the slot number
	 */
	public Integer getSlot()
	{
		return s;
	}
	
	/**
	 * Get the x coord for this slot
	 * 
	 * @return between -4 and 4, 0 being the center. -4 is the far left
	 */
	public Integer getX()
	{
		return x;
	}
	
	/**
	 * Get the y coord for this slot
	 * 
	 * @return between 1 and 6. 1 being the top, and 6 the far bottom
	 */
	public Integer getY()
	{
		return y;
	}
	
	private int getPosition(int x, int y)
	{
		return ((y - 1) * 9) + (x + 4);
	}
	
	/**
	 * Set the slot number for this slot object. Automatically updates the x and
	 * y coords
	 * 
	 * @param s
	 *            the slot
	 */
	public void setSlot(Integer s)
	{
		this.s = s;
		this.y = (int) ((s / 9) + 1);
		this.x = (s - ((y - 1) * 9)) - 4;
	}
	
	/**
	 * Set the slot for x. automatically updates the slot number
	 * 
	 * @param x
	 *            must be between -4 and 4, 0 being the center. -4 is the far
	 *            left
	 */
	public void setX(Integer x)
	{
		if(x > 4)
		{
			x = 4;
		}
		
		if(x < -4)
		{
			x = -4;
		}
		
		this.x = x;
		
		s = getPosition(x, y);
	}
	
	/**
	 * Set the slot for y. automatically updates the slot number
	 * 
	 * @param y
	 *            must be between 1 and 6. 1 being the top, and 6 the far bottom
	 */
	public void setY(Integer y)
	{
		if(y < 1)
		{
			y = 1;
		}
		
		this.y = y;
		
		s = getPosition(x, y);
	}
	
	public String toString()
	{
		return "Slot[" + getX() + "," + getY() + " (" + getSlot() + ")]";
	}
	
	public boolean equals(Object object)
	{
		if(object instanceof Slot)
		{
			return ((Slot) object).getSlot().equals(getSlot());
		}
		
		return false;
	}
}
package org.cyberpwn.phantom.gui;

public class Slot
{
	private Integer x;
	private Integer y;
	private Integer s;
	
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
	
	public Slot(Integer s)
	{
		this.s = s;
		this.y = (int) ((s / 9) + 1);
		this.x = (s - ((y - 1) * 9)) - 4;
	}
	
	public Integer getSlot()
	{
		return s;
	}
	
	public Integer getX()
	{
		return x;
	}
	
	public Integer getY()
	{
		return y;
	}
	
	private int getPosition(int x, int y)
	{
		return ((y - 1) * 9) + (x + 4);
	}
	
	public void setSlot(Integer s)
	{
		this.s = s;
		this.y = (int) ((s / 9) + 1);
		this.x = (s - ((y - 1) * 9)) - 4;
	}
	
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
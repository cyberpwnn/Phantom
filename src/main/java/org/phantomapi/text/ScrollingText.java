package org.phantomapi.text;

import org.phantomapi.util.F;

public class ScrollingText
{
	private String text;
	private int shift;
	
	public ScrollingText(String text)
	{
		this.text = text;
		shift = 0;
	}
	
	public ScrollingText()
	{
		text = "";
		shift = 0;
	}
	
	public void shiftLeft(int amt)
	{
		shift -= amt;
	}
	
	public void shiftRight(int amt)
	{
		shift += amt;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public int getShift()
	{
		return shift;
	}
	
	public void setShift(int shift)
	{
		this.shift = shift;
	}
	
	@Override
	public String toString()
	{
		if(shift == 0)
		{
			return text;
		}
		
		if(shift > 0)
		{
			return F.repeat(" ", shift) + text;
		}
		
		else
		{
			return text + F.repeat(" ", -shift);
		}
	}
}

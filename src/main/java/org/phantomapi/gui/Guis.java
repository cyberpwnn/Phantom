package org.phantomapi.gui;

import org.phantomapi.lang.GList;

/**
 * Gui utils
 * 
 * @author cyberpwn
 */
public class Guis
{
	/**
	 * Center the amount
	 * 
	 * @param amount
	 *            the amount
	 * @param level
	 *            the level
	 * @return a list of slots at the given level
	 */
	public static GList<Slot> getCentered(int amount, int level)
	{
		GList<Slot> s = new GList<Slot>();
		
		if(amount < 1 || level > 9)
		{
			return s;
		}
		
		if(amount % 2 != 0)
		{
			s.add(new Slot(0, level));
		}
		
		if(amount > 1)
		{
			s.add(new Slot(-1, level));
			s.add(new Slot(1, level));
		}
		
		if(amount > 3)
		{
			s.add(new Slot(-2, level));
			s.add(new Slot(2, level));
		}
		
		if(amount > 5)
		{
			s.add(new Slot(-3, level));
			s.add(new Slot(3, level));
		}
		
		if(amount > 7)
		{
			s.add(new Slot(-4, level));
			s.add(new Slot(4, level));
		}
		
		return s;
	}
	
	/**
	 * Sort the slots from left to right on one level
	 * 
	 * @param slots
	 *            the slots
	 * @return the ordered slots
	 */
	public static GList<Slot> sortLTR(GList<Slot> slots)
	{
		GList<Integer> x = new GList<Integer>();
		GList<Slot> s = new GList<Slot>();
		
		for(Slot i : slots)
		{
			x.add(i.getSlot());
		}
		
		x.sort();
		x.reverse();
		
		for(Integer i : x)
		{
			for(Slot j : slots)
			{
				if(i == j.getSlot())
				{
					s.add(j);
				}
			}
		}
		
		return s;
	}
}

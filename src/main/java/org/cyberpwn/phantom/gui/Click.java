package org.cyberpwn.phantom.gui;

import org.bukkit.event.inventory.ClickType;

public enum Click
{
	LEFT, RIGHT, MIDDLE, SHIFT_LEFT, SHIFT_RIGHT;
	
	public static Click fromClickType(ClickType type)
	{
		switch(type)
		{
			case LEFT:
				return LEFT;
			case MIDDLE:
				return MIDDLE;
			case RIGHT:
				return RIGHT;
			case SHIFT_LEFT:
				return SHIFT_LEFT;
			case SHIFT_RIGHT:
				return SHIFT_RIGHT;
			default:
				return null;
		}
	}
}

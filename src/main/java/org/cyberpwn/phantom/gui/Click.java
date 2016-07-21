package org.cyberpwn.phantom.gui;

import org.bukkit.event.inventory.ClickType;

/**
 * Click types
 * 
 * @author cyberpwn
 *
 */
public enum Click
{
	/**
	 * A Left click (pick up item)
	 */
	LEFT,
	
	/**
	 * A Right click (cut stack in half)
	 */
	RIGHT,
	
	/**
	 * A Middle mouse button click
	 */
	MIDDLE,
	
	/**
	 * Shift left click (transfer stack to another inventory)
	 */
	SHIFT_LEFT,
	
	/**
	 * Shift right click
	 */
	SHIFT_RIGHT;
	
	/**
	 * Transform ClickType into Click
	 * 
	 * @param type
	 *            the ClickType
	 * @return the Click enum, or null if not supported
	 */
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

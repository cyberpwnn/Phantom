package org.phantomapi.inventory;

import org.phantomapi.lang.GMap;

/**
 * Stacked inventory
 * @author cyberpwn
 *
 */
public interface StackedPhantomInventory
{
	/**
	 * Get an editable set of stacks. Be sure to thrash(); once you have made
	 * changes
	 * 
	 * @return the live set of stacks
	 */
	public GMap<Integer, Stack> getStacks();
	
	/**
	 * Set the stacks of an inventory. Automatically thrashes cached stacks,
	 * adds the items, then re-thrashes
	 * 
	 * @param stacks
	 *            the stacks
	 */
	public void setStacks(GMap<Integer, Stack> stacks);
	
	/**
	 * Set a stack to a slot
	 * 
	 * @param slot
	 *            the slot
	 * @param stack
	 *            the stack
	 */
	public void setStack(int slot, Stack stack);
	
	/**
	 * Get a stack from a slot
	 * 
	 * @param slot
	 *            the slot
	 * @return the stack
	 */
	public Stack getStack(int slot);
	
	/**
	 * Thrashes any cached stacks into the player inventory as item stacks
	 */
	public void thrash();
}

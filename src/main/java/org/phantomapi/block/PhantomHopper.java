package org.phantomapi.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Hopper;

/**
 * Hopper wrapper
 * 
 * @author cyberpwn
 */
public class PhantomHopper
{
	private Block hopper;
	
	/**
	 * Create a hopper
	 * 
	 * @param hopper
	 *            the hopper
	 */
	public PhantomHopper(Block hopper)
	{
		this.hopper = hopper;
	}
	
	/**
	 * Set the hopper direction (up is not compatible)
	 * 
	 * @param f
	 *            the facing direction
	 */
	public void setDirection(BlockFace f)
	{
		Hopper b = (Hopper) hopper.getState().getData();
		b.setFacingDirection(f);
		update();
	}
	
	/**
	 * Get the facing direction
	 * 
	 * @return the facing direction excluding up
	 */
	public BlockFace getDirection()
	{
		return ((Hopper) hopper.getState().getData()).getFacing();
	}
	
	/**
	 * Get the inventory of this hopper
	 * 
	 * @return the inventory object
	 */
	public Inventory getInventory()
	{
		return ((InventoryHolder) hopper.getState()).getInventory();
	}
	
	/**
	 * Update the hopper
	 */
	public void update()
	{
		hopper.getState().update();
	}
	
	/**
	 * Set the hopper to active or not
	 * 
	 * @param active
	 *            if true, the hopper can transfer items
	 */
	public void setActive(boolean active)
	{
		Hopper b = (Hopper) hopper.getState().getData();
		b.setActive(active);
		update();
	}
	
	/**
	 * Is the hopper active?
	 * 
	 * @return true if it is
	 */
	public boolean isActive()
	{
		return ((Hopper) hopper.getState().getData()).isActive();
	}
}

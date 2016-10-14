package org.phantomapi.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an event when an itemstack is swapped with another item stack
 * 
 * @author cyberpwn
 */
public class InventoryDropItemOnItemEvent extends InventoryEvent
{
	private final ItemStack target;
	private final ItemStack dropper;
	private final InventoryClickEvent evt;
	
	public InventoryDropItemOnItemEvent(Inventory inventory, Player player, ItemStack target, ItemStack dropper, InventoryClickEvent evt)
	{
		super(inventory, player);
		
		this.evt = evt;
		this.target = target;
		this.dropper = dropper;
	}
	
	public ItemStack getTarget()
	{
		return target;
	}
	
	public ItemStack getDropper()
	{
		return dropper;
	}
	
	/**
	 * Remove the dropper
	 */
	@SuppressWarnings("deprecation")
	public void clearDropper()
	{
		evt.setCursor(null);
	}
}

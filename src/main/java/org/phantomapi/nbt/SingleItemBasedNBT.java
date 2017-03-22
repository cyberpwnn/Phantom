package org.phantomapi.nbt;

import org.bukkit.inventory.ItemStack;

public interface SingleItemBasedNBT
{
	
	public ItemStack getItem();
	
	public void setItem(ItemStack item);
	
	public boolean isSet();
	
}

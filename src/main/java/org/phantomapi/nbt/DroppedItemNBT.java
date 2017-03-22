package org.phantomapi.nbt;

import org.bukkit.inventory.ItemStack;

public class DroppedItemNBT extends ItemNBT implements SingleItemBasedNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("DroppedItem");
		variables.add("Health", new ShortVariable("Health"));
		variables.add("PickupDelay", new ShortVariable("PickupDelay"));
		registerVariables(DroppedItemNBT.class, variables);
	}
	
	@Override
	public void setItem(ItemStack item)
	{
		if(item == null)
		{
			_data.remove("Item");
		}
		else
		{
			_data.setCompound("Item", NBTUtils.itemStackToNBTData(item));
		}
	}
	
	@Override
	public ItemStack getItem()
	{
		if(_data.hasKey("Item"))
		{
			return NBTUtils.itemStackFromNBTData(_data.getCompound("Item"));
		}
		return null;
	}
	
	@Override
	public boolean isSet()
	{
		return _data.hasKey("Item");
	}
	
}

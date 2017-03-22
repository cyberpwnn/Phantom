package org.phantomapi.nbt;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartContainerNBT extends MinecartNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("MinecartContainer");
		variables.add("LootTable", new StringVariable("LootTable"));
		variables.add("LootTableSeed", new LongVariable("LootTableSeed"));
		registerVariables(MinecartContainerNBT.class, variables);
	}
	
	public void setItemsFromInventory(Inventory inventory)
	{
		int l = Math.min(inventory.getSize(), getInventorySize());
		NBTTagList items = new NBTTagList();
		for(int i = 0; i < l; ++i)
		{
			ItemStack item = inventory.getItem(i);
			if(item != null)
			{
				NBTTagCompound itemNBT = NBTUtils.itemStackToNBTData(item);
				itemNBT.setByte("Slot", (byte) i);
				items.add(itemNBT);
			}
		}
		_data.setList("Items", items);
	}
	
	public void setItemsToInventory(Inventory inventory)
	{
		inventory.clear();
		if(_data.hasKey("Items"))
		{
			NBTTagList items = _data.getList("Items");
			int l = Math.min(items.size(), Math.min(inventory.getSize(), getInventorySize()));
			for(int i = 0; i < l; ++i)
			{
				NBTTagCompound itemNBT = (NBTTagCompound) items.get(i);
				inventory.setItem(itemNBT.getByte("Slot"), NBTUtils.itemStackFromNBTData(itemNBT));
			}
		}
	}
	
	public int getInventorySize()
	{
		if(getEntityType() == EntityType.MINECART_CHEST)
		{
			return 27;
		}
		else if(getEntityType() == EntityType.MINECART_HOPPER)
		{
			return 5;
		}
		return 0;
	}
	
}

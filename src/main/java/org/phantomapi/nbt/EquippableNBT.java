package org.phantomapi.nbt;

import java.util.Arrays;
import org.bukkit.inventory.ItemStack;

public class EquippableNBT extends EntityNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Equippable");
		variables.add("FallFlying", new BooleanVariable("FallFlying"));
		registerVariables(EquippableNBT.class, variables);
	}
	
	private void setItems(String key, ItemStack... items)
	{
		if(items == null)
		{
			_data.remove(key);
			return;
		}
		Object[] data = new Object[items.length];
		boolean allNull = true;
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] == null)
			{
				data[i] = new NBTTagCompound();
			}
			else
			{
				data[i] = NBTUtils.itemStackToNBTData(items[i]);
				allNull = false;
			}
		}
		if(allNull)
		{
			_data.remove(key);
		}
		else
		{
			_data.setList(key, data);
		}
	}
	
	private ItemStack[] getItems(String key, int size)
	{
		ItemStack[] items = new ItemStack[size];
		Object[] data = _data.getListAsArray(key);
		if(data != null)
		{
			for(int i = 0; i < data.length; i++)
			{
				if(data[i] != null && data[i] instanceof NBTTagCompound)
				{
					items[i] = NBTUtils.itemStackFromNBTData((NBTTagCompound) data[i]);
				}
			}
		}
		return items;
	}
	
	public void setArmorItems(ItemStack feet, ItemStack legs, ItemStack chest, ItemStack head)
	{
		setItems("ArmorItems", feet, legs, chest, head);
	}
	
	public ItemStack[] getArmorItems()
	{
		return getItems("ArmorItems", 4);
	}
	
	public void setHandItems(ItemStack main, ItemStack off)
	{
		setItems("HandItems", main, off);
	}
	
	public ItemStack[] getHandItems()
	{
		return getItems("HandItems", 2);
	}
	
	@Override
	void onUnserialize()
	{
		super.onUnserialize();
		// Backward compatibility with pre-1.9.
		if(_data.hasKey("Equipment"))
		{
			Object[] equip = _data.getListAsArray("Equipment");
			_data.setList("HandItems", new NBTTagList(equip[0], new NBTTagCompound()));
			_data.setList("ArmorItems", new NBTTagList(Arrays.copyOfRange(equip, 1, 5)));
			_data.remove("Equipment");
		}
	}
	
}

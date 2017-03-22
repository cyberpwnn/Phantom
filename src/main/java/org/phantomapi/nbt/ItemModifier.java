package org.phantomapi.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

public final class ItemModifier extends Modifier
{
	
	private AttributeType _attributeType;
	private String _slot;
	
	public static ItemModifier fromNBT(NBTTagCompound data)
	{
		return new ItemModifier(AttributeType.getByInternalName(data.getString("AttributeName")), data.getString("Name"), data.getDouble("Amount"), data.getInt("Operation"), (data.hasKey("Slot") ? data.getString("Slot") : null), new UUID(data.getLong("UUIDMost"), data.getLong("UUIDLeast")));
	}
	
	public static List<ItemModifier> getItemStackModifiers(ItemStack item)
	{
		NBTTagCompound tag = NBTUtils.getItemStackTag(item);
		if(tag.hasKey("AttributeModifiers"))
		{
			Object[] modifiersData = tag.getListAsArray("AttributeModifiers");
			List<ItemModifier> modifiers = new ArrayList<ItemModifier>(modifiersData.length);
			for(Object data : modifiersData)
			{
				modifiers.add(fromNBT((NBTTagCompound) data));
			}
			return modifiers;
		}
		return new ArrayList<ItemModifier>();
	}
	
	public static void setItemStackModifiers(ItemStack item, List<ItemModifier> modifiers)
	{
		NBTTagList modifiersData = new NBTTagList();
		for(ItemModifier modifier : modifiers)
		{
			modifiersData.add(modifier.toNBT());
		}
		NBTTagCompound tag = NBTUtils.getItemStackTag(item);
		tag.setList("AttributeModifiers", modifiersData);
		NBTUtils.setItemStackTag(item, tag);
	}
	
	public ItemModifier(AttributeType attributeType, String name, double amount, int operation, String slot)
	{
		super(name, amount, operation);
		_attributeType = attributeType;
		_slot = slot;
	}
	
	public ItemModifier(AttributeType attributeType, String name, double amount, int operation, String slot, UUID uuid)
	{
		super(name, amount, operation, uuid);
		_attributeType = attributeType;
		_slot = slot;
	}
	
	public AttributeType getAttributeType()
	{
		return _attributeType;
	}
	
	@Override
	public NBTTagCompound toNBT()
	{
		NBTTagCompound data = super.toNBT();
		data.setString("AttributeName", _attributeType._internalName);
		if(_slot != null)
		{
			data.setString("Slot", _slot);
		}
		return data;
	}
	
}

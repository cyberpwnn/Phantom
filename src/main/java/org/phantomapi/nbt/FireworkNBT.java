package org.phantomapi.nbt;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public final class FireworkNBT extends EntityNBT implements SingleItemBasedNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Firework");
		variables.add("Life", new IntegerVariable("Life", 0, 200)); // Limited
																	// to 200
		variables.add("Lifetime", new IntegerVariable("LifeTime", 0, 200)); // Limited
																			// to
																			// 200
		registerVariables(FireworkNBT.class, variables);
	}
	
	public FireworkNBT()
	{
		super(EntityType.FIREWORK);
	}
	
	public FireworkNBT(ItemStack firework)
	{
		this();
		if(firework.getType() != Material.FIREWORK)
		{
			throw new IllegalArgumentException("Invalid argument firework.");
		}
		_data.setInt("Life", 0);
		_data.setCompound("FireworksItem", NBTUtils.itemStackToNBTData(firework));
		setLifeTimeFromItem(firework);
	}
	
	private void setLifeTimeFromItem(ItemStack firework)
	{
		if(firework == null)
		{
			_data.remove("FireworksItem");
		}
		else
		{
			_data.setInt("LifeTime", 12 + 12 * ((FireworkMeta) firework.getItemMeta()).getPower());
		}
	}
	
	@Override
	public void setItem(ItemStack firework)
	{
		if(firework == null)
		{
			_data.remove("FireworksItem");
		}
		else
		{
			_data.setCompound("FireworksItem", NBTUtils.itemStackToNBTData(firework));
		}
		setLifeTimeFromItem(firework);
	}
	
	@Override
	public ItemStack getItem()
	{
		if(_data.hasKey("FireworksItem"))
		{
			return NBTUtils.itemStackFromNBTData(_data.getCompound("FireworksItem"));
		}
		return null;
	}
	
	@Override
	public boolean isSet()
	{
		return _data.hasKey("FireworksItem");
	}
	
}

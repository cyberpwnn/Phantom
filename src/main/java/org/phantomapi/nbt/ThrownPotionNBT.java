package org.phantomapi.nbt;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ThrownPotionNBT extends EntityNBT implements SingleItemBasedNBT
{
	
	@Override
	public void setItem(ItemStack potion)
	{
		if(potion == null)
		{
			_data.remove("Potion");
		}
		else
		{
			NBTTagCompound data = NBTUtils.itemStackToNBTData(potion);
			Material type = potion.getType();
			if(type != Material.SPLASH_POTION && type != Material.LINGERING_POTION)
			{
				data.setString("id", "minecraft:splash_potion");
			}
			_data.setCompound("Potion", data);
		}
	}
	
	@Override
	public ItemStack getItem()
	{
		if(_data.hasKey("Potion"))
		{
			return NBTUtils.itemStackFromNBTData(_data.getCompound("Potion"));
		}
		return null;
	}
	
	@Override
	public boolean isSet()
	{
		return _data.hasKey("Potion");
	}
	
}

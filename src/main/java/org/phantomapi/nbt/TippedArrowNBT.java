package org.phantomapi.nbt;

import org.bukkit.inventory.ItemStack;

public class TippedArrowNBT extends ThrownPotionNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("TippedArrow");
		variables.add("Potion", new StringVariable("Potion"));
		registerVariables(TippedArrowNBT.class, variables);
	}
	
	@Override
	public void setItem(ItemStack potion)
	{
		if(potion == null)
		{
			_data.remove("CustomPotionEffects");
		}
		else
		{
			NBTTagList effects = NBTUtils.potionToNBTEffectsList(potion);
			if(effects != null)
			{
				_data.setList("CustomPotionEffects", effects);
				return;
			}
		}
	}
	
	@Override
	public ItemStack getItem()
	{
		if(_data.hasKey("CustomPotionEffects"))
		{
			return NBTUtils.potionFromNBTEffectsList(_data.getList("CustomPotionEffects"));
		}
		return null;
	}
	
	@Override
	public boolean isSet()
	{
		return _data.hasKey("CustomPotionEffects");
	}
	
}

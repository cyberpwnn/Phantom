package org.phantomapi.nbt;

import org.bukkit.inventory.ItemStack;

public class AreaEffectCloudNBT extends ThrownPotionNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("AreaEffectCloud");
		variables.add("Age", new IntegerVariable("Age", 0));
		variables.add("Color", new ColorVariable("Color"));
		variables.add("Duration", new IntegerVariable("Duration", 0));
		variables.add("ReapplicationDelay", new IntegerVariable("ReapplicationDelay", 0));
		variables.add("WaitTime", new IntegerVariable("WaitTime", 0));
		variables.add("Radius", new FloatVariable("Radius", 0f));
		variables.add("RadiusOnUse", new FloatVariable("RadiusOnUse"));
		variables.add("RadiusPerTick", new FloatVariable("RadiusPerTick"));
		variables.add("Particle", new ParticleVariable("Particle"));
		variables.add("ParticleParam1", new IntegerVariable("ParticleParam1"));
		variables.add("ParticleParam2", new IntegerVariable("ParticleParam2"));
		variables.add("Potion", new StringVariable("Potion"));
		registerVariables(AreaEffectCloudNBT.class, variables);
	}
	
	public AreaEffectCloudNBT()
	{
		_data.setInt("Duration", 500);
		_data.setInt("ReapplicationDelay", 10);
		_data.setInt("Radius", 3);
	}
	
	@Override
	public void setItem(ItemStack potion)
	{
		if(potion == null)
		{
			_data.remove("Effects");
		}
		else
		{
			NBTTagList effects = NBTUtils.potionToNBTEffectsList(potion);
			if(effects != null)
			{
				_data.setList("Effects", effects);
				return;
			}
		}
	}
	
	@Override
	public ItemStack getItem()
	{
		if(_data.hasKey("Effects"))
		{
			return NBTUtils.potionFromNBTEffectsList(_data.getList("Effects"));
		}
		return null;
	}
	
	@Override
	public boolean isSet()
	{
		return _data.hasKey("Effects");
	}
	
}

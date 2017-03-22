package org.phantomapi.nbt;

public class ZombieNBT extends MobNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Zombie");
		variables.add("Type", new IntegerVariable("ZombieType", 0, 6));
		variables.add("IsBaby", new BooleanVariable("IsBaby"));
		variables.add("ConversionTime", new IntegerVariable("ConversionTime", -1));
		variables.add("CanBreakDoors", new BooleanVariable("CanBreakDoors"));
		registerVariables(ZombieNBT.class, variables);
	}
	
	@Override
	void onUnserialize()
	{
		super.onUnserialize();
		// Backward compatibility with pre-1.10.
		if(_data.hasKey("IsVillager"))
		{
			if(_data.getByte("IsVillager") != 0)
			{
				if(_data.hasKey("VillagerProfession"))
				{
					_data.setInt("ZombieType", _data.getInt("VillagerProfession") + 1);
					_data.remove("VillagerProfession");
				}
				else
				{
					_data.setInt("ZombieType", 1);
				}
			}
			_data.remove("IsVillager");
		}
	}
	
}

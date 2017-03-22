package org.phantomapi.nbt;

import org.bukkit.entity.EntityType;

public class XPOrbNBT extends ItemNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("XPOrb");
		variables.add("Health", new ByteVariable("Health"));
		variables.add("Value", new ShortVariable("Value"));
		registerVariables(EntityType.EXPERIENCE_ORB, variables);
	}
	
}

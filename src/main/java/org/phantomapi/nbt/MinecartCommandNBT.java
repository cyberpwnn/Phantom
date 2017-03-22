package org.phantomapi.nbt;

import org.bukkit.entity.EntityType;

public class MinecartCommandNBT extends MinecartNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("MinecartCommandBlock");
		variables.add("Command", new StringVariable("Command"));
		registerVariables(EntityType.MINECART_COMMAND, variables);
	}
	
}

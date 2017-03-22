package org.phantomapi.nbt;

public class ItemNBT extends EntityNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Item");
		variables.add("Age", new ShortVariable("Age"));
		registerVariables(ItemNBT.class, variables);
	}
	
}

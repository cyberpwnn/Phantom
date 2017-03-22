package org.phantomapi.nbt;

public class SlimeNBT extends MobNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Slime");
		variables.add("Size", new IntegerVariable("Size", -50, 50)); // Limited
																		// to 50
		registerVariables(SlimeNBT.class, variables);
	}
	
}

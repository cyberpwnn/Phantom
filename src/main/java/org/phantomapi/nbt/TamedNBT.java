package org.phantomapi.nbt;

public class TamedNBT extends BreedNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Tameable");
		variables.add("Owner", new StringVariable("Owner"));
		variables.add("Sitting", new BooleanVariable("Sitting"));
		registerVariables(TamedNBT.class, variables);
	}
	
}

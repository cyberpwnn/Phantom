package org.phantomapi.nbt;

public class FireballNBT extends EntityNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Fireball");
		variables.add("Direction", new VectorVariable("direction"));
		variables.add("Power", new VectorVariable("power"));
		registerVariables(FireballNBT.class, variables);
	}
	
	public FireballNBT()
	{
		_data.setList("direction", 0.0d, 0.0d, 0.0d);
		_data.setList("power", 0.0d, 0.0d, 0.0d);
	}
	
}

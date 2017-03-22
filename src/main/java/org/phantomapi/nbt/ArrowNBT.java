package org.phantomapi.nbt;

public class ArrowNBT extends EntityNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Arrow");
		variables.add("Pickup", new ByteVariable("pickup", (byte) 0, (byte) 2));
		variables.add("Player", new BooleanVariable("player"));
		variables.add("Life", new ShortVariable("life"));
		variables.add("Damage", new DoubleVariable("damage"));
		registerVariables(ArrowNBT.class, variables);
	}
	
}

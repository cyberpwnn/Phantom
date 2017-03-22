package org.phantomapi.nbt;

public final class HorseNBT extends BreedNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Horse");
		variables.add("Type", new IntegerVariable("Type", 0, 4));
		variables.add("Tamed", new BooleanVariable("Tame"));
		variables.add("Chested", new BooleanVariable("ChestedHorse"));
		variables.add("Eating", new BooleanVariable("EatingHaystack"));
		variables.add("Owner", new StringVariable("OwnerName"));
		variables.add("Variant", new HorseVariantVariable());
		registerVariables(HorseNBT.class, variables);
	}
	
}

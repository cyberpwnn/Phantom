package org.phantomapi.nbt;

public class MinecartNBT extends EntityNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Minecart");
		variables.add("DisplayTile", new BooleanVariable("CustomDisplayTile"));
		variables.add("Tile", new BlockVariable("DisplayTile", "DisplayData", false, true));
		variables.add("TileOffset", new IntegerVariable("DisplayOffset"));
		variables.add("Name", new StringVariable("CustomName"));
		registerVariables(MinecartNBT.class, variables);
	}
	
}

package org.phantomapi.nbt;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public class MinecartSpawnerNBT extends MinecartNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("MinecartSpawner");
		variables.add("Count", new ShortVariable("SpawnCount", (short) 0));
		variables.add("Range", new ShortVariable("SpawnRange", (short) 0));
		variables.add("Delay", new ShortVariable("Delay", (short) 0));
		variables.add("MinDelay", new ShortVariable("MinSpawnDelay", (short) 0));
		variables.add("MaxDelay", new ShortVariable("MaxSpawnDelay", (short) 0));
		variables.add("MaxEntities", new ShortVariable("MaxNearbyEntities", (short) 0));
		variables.add("PlayerRange", new ShortVariable("RequiredPlayerRange", (short) 0));
		registerVariables(EntityType.MINECART_MOB_SPAWNER, variables);
	}
	
	public void copyFromSpawner(Block block)
	{
		NBTTagCompound data = NBTUtils.getTileEntityNBTData(block);
		data.remove("id");
		data.remove("x");
		data.remove("y");
		data.remove("z");
		_data.remove("SpawnData");
		_data.remove("SpawnPotentials");
		_data.merge(data);
	}
	
	public void copyToSpawner(Block block)
	{
		NBTTagCompound data = NBTUtils.getTileEntityNBTData(block);
		data.remove("SpawnData");
		data.remove("SpawnPotentials");
		data.merge(_data);
		NBTUtils.setTileEntityNBTData(block, data);
	}
	
}

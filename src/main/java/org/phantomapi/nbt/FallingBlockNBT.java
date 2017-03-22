package org.phantomapi.nbt;

import org.bukkit.block.Block;

public class FallingBlockNBT extends EntityNBT
{
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("FallingBlock");
		variables.add("Block", new BlockVariable("TileID", "Data"));
		variables.add("Time", new ByteVariable("Time", (byte) 0));
		variables.add("DropItem", new BooleanVariable("DropItem"));
		variables.add("HurtEntities", new BooleanVariable("HurtEntities"));
		variables.add("FallHurtAmount", new FloatVariable("FallHurtAmount", 0));
		variables.add("FallHurtMax", new IntegerVariable("FallHurtMax", 0));
		registerVariables(FallingBlockNBT.class, variables);
	}
	
	public FallingBlockNBT()
	{
		_data.setByte("Time", (byte) 1);
	}
	
	@SuppressWarnings("deprecation")
	public void copyFromTileEntity(Block block)
	{
		_data.setInt("TileID", block.getTypeId());
		_data.setByte("Data", block.getData());
		NBTTagCompound tileEntityData = NBTUtils.getTileEntityNBTData(block);
		if(tileEntityData != null)
		{
			_data.setCompound("TileEntityData", tileEntityData);
		}
		else
		{
			_data.remove("TileEntityData");
		}
	}
	
	public boolean hasTileEntityData()
	{
		return _data.hasKey("TileEntityData");
	}
	
}

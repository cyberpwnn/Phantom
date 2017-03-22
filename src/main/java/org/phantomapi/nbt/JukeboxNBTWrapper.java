package org.phantomapi.nbt;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public final class JukeboxNBTWrapper extends TileNBTWrapper
{
	
	public JukeboxNBTWrapper(Block block)
	{
		super(block);
	}
	
	@SuppressWarnings("deprecation")
	public void setRecord(ItemStack item)
	{
		if(item == null || item.getType() == Material.AIR)
		{
			_data.setInt("Record", 0);
			_data.setCompound("RecordItem", new NBTTagCompound());
		}
		else
		{
			_data.setInt("Record", item.getTypeId());
			_data.setCompound("RecordItem", NBTUtils.itemStackToNBTData(item));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void save()
	{
		if(_data.getInt("Record") != 0 && _block.getData() == 0)
		{
			_block.setData((byte) 1);
		}
		else if(_data.getInt("Record") == 0 && _block.getData() != 0)
		{
			_block.setData((byte) 0);
		}
		super.save();
	}
	
}

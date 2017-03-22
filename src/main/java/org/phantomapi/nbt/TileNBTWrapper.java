package org.phantomapi.nbt;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class TileNBTWrapper
{
	
	protected Block _block;
	protected NBTTagCompound _data;
	
	public static final boolean allowsCustomName(Material mat)
	{
		return (mat == Material.CHEST || mat == Material.FURNACE || mat == Material.DISPENSER || mat == Material.DROPPER || mat == Material.HOPPER || mat == Material.BREWING_STAND || mat == Material.ENCHANTMENT_TABLE || mat == Material.COMMAND);
	}
	
	public TileNBTWrapper(Block block)
	{
		_block = block;
		_data = NBTUtils.getTileEntityNBTData(_block);
	}
	
	public final boolean allowsCustomName()
	{
		return allowsCustomName(_block.getType());
	}
	
	public final void setCustomName(String name)
	{
		if(allowsCustomName())
		{
			if(name == null)
			{
				_data.setString("CustomName", "");
			}
			else
			{
				_data.setString("CustomName", name);
			}
		}
	}
	
	public final String getCustomName()
	{
		return (allowsCustomName() ? _data.getString("CustomName") : null);
	}
	
	public final Location getLocation()
	{
		return _block.getLocation();
	}
	
	public void save()
	{
		NBTUtils.setTileEntityNBTData(_block, _data);
	}
	
}

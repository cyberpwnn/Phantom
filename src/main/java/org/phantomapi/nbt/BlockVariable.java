package org.phantomapi.nbt;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class BlockVariable extends NBTGenericVariable2X
{
	
	private boolean _asShort;
	private boolean _dataAsInt;
	
	public BlockVariable(String blockNbtKey, String dataNbtKey)
	{
		this(blockNbtKey, dataNbtKey, false);
	}
	
	public BlockVariable(String blockNbtKey, String dataNbtKey, boolean asShort)
	{
		this(blockNbtKey, dataNbtKey, asShort, false);
	}
	
	public BlockVariable(String blockNbtKey, String dataNbtKey, boolean asShort, boolean dataAsInt)
	{
		super(blockNbtKey, dataNbtKey);
		_asShort = asShort;
		_dataAsInt = dataAsInt;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		String[] pieces = value.split(" ", 2);
		Material material = MaterialMap.getByName(pieces[0]);
		if(material == null)
		{
			material = MaterialMap.getByName("minecraft:" + pieces[0]);
		}
		if(material == null)
		{
			try
			{
				int blockId = Integer.parseInt(pieces[0]);
				material = Material.getMaterial(blockId);
			}
			catch(NumberFormatException e)
			{
				return false;
			}
		}
		if(material != null && material.isBlock())
		{
			int blockData = 0;
			if(pieces.length == 2)
			{
				try
				{
					blockData = Integer.parseInt(pieces[1]);
					if(blockData < 0 || blockData > 0xFF)
					{
						return false;
					}
				}
				catch(NumberFormatException e)
				{
					return false;
				}
			}
			if(_asShort)
			{
				data.setShort(_nbtKey, (short) material.getId());
				data.setShort(_nbtKey2, (short) blockData);
			}
			else
			{
				data.setInt(_nbtKey, material.getId());
				if(_dataAsInt)
				{
					data.setInt(_nbtKey2, (byte) (blockData & 0xFF));
				}
				else
				{
					data.setByte(_nbtKey2, (byte) (blockData & 0xFF));
				}
			}
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	String get(NBTTagCompound data)
	{
		if(data.hasKey(_nbtKey) && data.hasKey(_nbtKey2))
		{
			int materialId, materialData;
			if(_asShort)
			{
				materialId = data.getShort(_nbtKey) & 0xFF;
				materialData = data.getShort(_nbtKey2) & 0xFF;
			}
			else
			{
				materialId = data.getInt(_nbtKey);
				if(_dataAsInt)
				{
					materialData = data.getInt(_nbtKey2) & 0xFF;
				}
				else
				{
					materialData = data.getByte(_nbtKey2) & 0xFF;
				}
			}
			return MaterialMap.getName(Material.getMaterial(materialId)) + " " + materialData;
		}
		return null;
	}
	
	@Override
	String getFormat()
	{
		return "Valid block name/id and data, '<name/id> [data]'.";
	}
	
	@Override
	public List<String> getPossibleValues()
	{
		return MaterialMap.getBlockNames();
	}
	
}

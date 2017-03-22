package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public final class ByteVariable extends NumericVariable
{
	
	public ByteVariable(String nbtKey)
	{
		this(nbtKey, Byte.MIN_VALUE);
	}
	
	public ByteVariable(String nbtKey, byte min)
	{
		this(nbtKey, min, Byte.MAX_VALUE);
	}
	
	public ByteVariable(String nbtKey, byte min, byte max)
	{
		super(nbtKey, min, max);
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		try
		{
			byte v = Byte.parseByte(value);
			if(v < _min || v > _max)
			{
				return false;
			}
			data.setByte(_nbtKey, v);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	@Override
	String get(NBTTagCompound data)
	{
		if(data.hasKey(_nbtKey))
		{
			return String.valueOf(data.getByte(_nbtKey));
		}
		return null;
	}
	
}

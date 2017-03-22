package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public final class LongVariable extends NumericVariable
{
	
	public LongVariable(String nbtKey)
	{
		this(nbtKey, Long.MIN_VALUE);
	}
	
	public LongVariable(String nbtKey, long min)
	{
		this(nbtKey, min, Long.MAX_VALUE);
	}
	
	public LongVariable(String nbtKey, long min, long max)
	{
		super(nbtKey, min, max);
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		try
		{
			long v = Long.parseLong(value);
			if(v < _min || v > _max)
			{
				return false;
			}
			data.setLong(_nbtKey, v);
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
			return String.valueOf(data.getLong(_nbtKey));
		}
		return null;
	}
	
}

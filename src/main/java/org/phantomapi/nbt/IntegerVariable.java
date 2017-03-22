package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public final class IntegerVariable extends NumericVariable
{
	
	public IntegerVariable(String nbtKey)
	{
		this(nbtKey, Integer.MIN_VALUE);
	}
	
	public IntegerVariable(String nbtKey, int min)
	{
		this(nbtKey, min, Integer.MAX_VALUE);
	}
	
	public IntegerVariable(String nbtKey, int min, int max)
	{
		super(nbtKey, min, max);
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		try
		{
			int v = Integer.parseInt(value);
			if(v < _min || v > _max)
			{
				return false;
			}
			data.setInt(_nbtKey, v);
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
			return String.valueOf(data.getInt(_nbtKey));
		}
		return null;
	}
	
}

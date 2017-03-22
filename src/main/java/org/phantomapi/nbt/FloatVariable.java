package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public final class FloatVariable extends NBTGenericVariable
{
	
	private float _min;
	float _max;
	
	public FloatVariable(String nbtKey)
	{
		this(nbtKey, -Float.MAX_VALUE);
	}
	
	public FloatVariable(String nbtKey, float min)
	{
		this(nbtKey, min, Float.MAX_VALUE);
	}
	
	public FloatVariable(String nbtKey, float min, float max)
	{
		super(nbtKey);
		_min = min;
		_max = max;
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		try
		{
			float v = Float.parseFloat(value);
			if(v < _min || v > _max)
			{
				return false;
			}
			data.setFloat(_nbtKey, v);
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
			return String.valueOf(data.getFloat(_nbtKey));
		}
		return null;
	}
	
	@Override
	String getFormat()
	{
		return String.format("Decimal between %s and %s.", _min, _max);
	}
	
}

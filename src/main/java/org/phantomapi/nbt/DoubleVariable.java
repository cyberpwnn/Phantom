package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public final class DoubleVariable extends NBTGenericVariable
{
	
	public DoubleVariable(String nbtKey)
	{
		super(nbtKey);
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		try
		{
			data.setDouble(_nbtKey, Double.parseDouble(value));
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
			return String.valueOf(data.getDouble(_nbtKey));
		}
		return null;
	}
	
	@Override
	String getFormat()
	{
		return "Decimal.";
	}
	
}

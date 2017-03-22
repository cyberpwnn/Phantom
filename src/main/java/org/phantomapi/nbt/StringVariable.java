package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public class StringVariable extends NBTGenericVariable
{
	
	public StringVariable(String nbtKey)
	{
		super(nbtKey);
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		if(value.length() > 64)
		{
			return false;
		}
		data.setString(_nbtKey, value);
		return true;
	}
	
	@Override
	String get(NBTTagCompound data)
	{
		if(data.hasKey(_nbtKey))
		{
			return data.getString(_nbtKey);
		}
		return null;
	}
	
	@Override
	String getFormat()
	{
		return "String (max length: 64).";
	}
	
}

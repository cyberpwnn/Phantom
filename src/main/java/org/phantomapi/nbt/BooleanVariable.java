package org.phantomapi.nbt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;

public final class BooleanVariable extends NBTGenericVariable
{
	
	private static final List<String> POSSIBLE_VALUES = Collections.unmodifiableList(Arrays.asList(new String[] {"true", "false"}));
	
	public BooleanVariable(String nbtKey)
	{
		super(nbtKey);
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		String lower = value.toLowerCase();
		if("true".startsWith(lower))
		{
			data.setByte(_nbtKey, (byte) 1);
		}
		else if("false".startsWith(lower))
		{
			data.setByte(_nbtKey, (byte) 0);
		}
		else
		{
			return false;
		}
		return true;
	}
	
	@Override
	String get(NBTTagCompound data)
	{
		if(data.hasKey(_nbtKey))
		{
			return (data.getByte(_nbtKey) > 0 ? "true" : "false");
		}
		return null;
	}
	
	@Override
	String getFormat()
	{
		return "Boolean value 'true' or 'false'.";
	}
	
	@Override
	public List<String> getPossibleValues()
	{
		return POSSIBLE_VALUES;
	}
	
}

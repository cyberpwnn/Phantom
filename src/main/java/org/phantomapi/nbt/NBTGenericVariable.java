package org.phantomapi.nbt;

import java.util.List;
import org.bukkit.entity.Player;

public abstract class NBTGenericVariable
{
	
	protected String _nbtKey;
	
	NBTGenericVariable(String nbtKey)
	{
		_nbtKey = nbtKey;
	}
	
	abstract boolean set(NBTTagCompound data, String value, Player player);
	
	abstract String get(NBTTagCompound data);
	
	void clear(NBTTagCompound data)
	{
		data.remove(_nbtKey);
	}
	
	abstract String getFormat();
	
	public List<String> getPossibleValues()
	{
		return null;
	}
	
}

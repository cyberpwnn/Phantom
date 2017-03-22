package org.phantomapi.nbt;

import java.util.List;
import org.bukkit.entity.Player;

public final class NBTVariable
{
	
	private String _name;
	private NBTGenericVariable _generic;
	private NBTTagCompound _data;
	
	NBTVariable(String name, NBTGenericVariable generic, NBTTagCompound data)
	{
		_name = name;
		_generic = generic;
		_data = data;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public boolean setValue(String value)
	{
		return _generic.set(_data, value, null);
	}
	
	public boolean setValue(String value, Player player)
	{
		return _generic.set(_data, value, player);
	}
	
	public String getValue()
	{
		return _generic.get(_data);
	}
	
	public void clear()
	{
		_generic.clear(_data);
	}
	
	public String getFormat()
	{
		return _generic.getFormat();
	}
	
	public List<String> getPossibleValues()
	{
		return _generic.getPossibleValues();
	}
	
}

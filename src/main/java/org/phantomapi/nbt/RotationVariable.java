package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public class RotationVariable extends NBTGenericVariable
{
	
	private int _count;
	private String _parentNbtKey;
	
	public RotationVariable(String nbtKey, boolean triple, String parentNbtKey)
	{
		super(nbtKey);
		_count = (triple ? 3 : 2);
		_parentNbtKey = parentNbtKey;
	}
	
	public RotationVariable(String nbtKey)
	{
		this(nbtKey, false, null);
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		String[] pieces = value.replace(',', '.').split("\\s+", _count);
		if(pieces.length == _count)
		{
			Object[] values = new Object[_count];
			try
			{
				for(int i = 0; i < _count; i++)
				{
					values[i] = Float.parseFloat(pieces[i]);
				}
			}
			catch(NumberFormatException e)
			{
				return false;
			}
			if(_parentNbtKey != null)
			{
				NBTTagCompound subData = data.getCompound(_parentNbtKey);
				if(subData == null)
				{
					subData = new NBTTagCompound();
					data.setCompound(_parentNbtKey, subData);
				}
				data = subData;
			}
			data.setList(_nbtKey, values);
			return true;
		}
		return false;
	}
	
	@Override
	String get(NBTTagCompound data)
	{
		if(_parentNbtKey != null)
		{
			data = data.getCompound(_parentNbtKey);
			if(data == null)
			{
				return null;
			}
		}
		if(data.hasKey(_nbtKey))
		{
			Object[] vector = data.getListAsArray(_nbtKey);
			if(vector.length == 2)
			{
				return (Float) vector[0] + " " + (Float) vector[1];
			}
			else if(vector.length == 3)
			{
				return (Float) vector[0] + " " + (Float) vector[1] + " " + (Float) vector[2];
			}
		}
		return null;
	}
	
	@Override
	void clear(NBTTagCompound data)
	{
		if(_parentNbtKey == null)
		{
			super.clear(data);
		}
		else
		{
			NBTTagCompound subData = data.getCompound(_parentNbtKey);
			if(subData != null)
			{
				subData.remove(_nbtKey);
				if(subData.isEmpty())
				{
					data.remove(_parentNbtKey);
				}
			}
		}
	}
	
	@Override
	String getFormat()
	{
		if(_count == 3)
		{
			return "Set of 3 decimal numbers: x , y and z angles, e.g. '25.6 -90 23'.";
		}
		return "Set of 2 decimal numbers: yaw and pitch angles, e.g. '25.6 -90'.";
	}
	
}

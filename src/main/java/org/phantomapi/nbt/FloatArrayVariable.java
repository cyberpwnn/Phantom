package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public class FloatArrayVariable extends NBTGenericVariable
{
	
	private int _count;
	private float _min;
	private float _max;
	
	public FloatArrayVariable(String nbtKey, int count, float min, float max)
	{
		super(nbtKey);
		_count = Math.max(0, Math.min(10, count));
		_min = min;
		_max = max;
	}
	
	public FloatArrayVariable(String nbtKey, int count)
	{
		this(nbtKey, count, -Float.MAX_VALUE, Float.MIN_VALUE);
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
					float v = Float.parseFloat(pieces[i]);
					if(v < _min || v > _max)
					{
						return false;
					}
					values[i] = v;
				}
			}
			catch(NumberFormatException e)
			{
				return false;
			}
			data.setList(_nbtKey, values);
			return true;
		}
		return false;
	}
	
	@Override
	String get(NBTTagCompound data)
	{
		if(data.hasKey(_nbtKey))
		{
			Object[] vector = data.getListAsArray(_nbtKey);
			StringBuilder sb = new StringBuilder();
			for(Object v : vector)
			{
				if(sb.length() != 0)
				{
					sb.append(" ");
				}
				sb.append(v);
			}
			return sb.toString();
		}
		return null;
	}
	
	@Override
	String getFormat()
	{
		return String.format("Set of %s decimal numbers between %s and %s.", _count, _min, _max);
	}
	
}

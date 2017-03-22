package org.phantomapi.nbt;

public abstract class NumericVariable extends NBTGenericVariable
{
	
	protected long _min;
	protected long _max;
	
	public NumericVariable(String nbtKey, long min, long max)
	{
		super(nbtKey);
		_min = min;
		_max = max;
	}
	
	@Override
	String getFormat()
	{
		return String.format("Integer between %s and %s.", _min, _max);
	}
	
}

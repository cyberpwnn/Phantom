package org.phantomapi.nbt;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class NBTVariableIterator implements Iterator<NBTVariable>
{
	
	Iterator<Entry<String, NBTGenericVariable>> _state;
	NBTTagCompound _data;
	String _separator;
	
	NBTVariableIterator(LinkedHashMap<String, NBTGenericVariable> hashMap, NBTTagCompound data)
	{
		_state = hashMap.entrySet().iterator();
		_data = data;
	}
	
	@Override
	public boolean hasNext()
	{
		return _state.hasNext();
	}
	
	@Override
	public NBTVariable next()
	{
		Entry<String, NBTGenericVariable> entry = _state.next();
		return new NBTVariable(entry.getKey(), entry.getValue(), _data);
	}
	
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Cannot remove NBTVariables.");
	}
	
}

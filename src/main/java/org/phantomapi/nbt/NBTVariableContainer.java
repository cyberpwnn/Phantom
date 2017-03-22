package org.phantomapi.nbt;

import java.util.Iterator;
import java.util.Set;

public final class NBTVariableContainer implements Iterable<NBTVariable>
{
	
	NBTGenericVariableContainer _generic;
	NBTTagCompound _data;
	
	NBTVariableContainer(NBTGenericVariableContainer generic, NBTTagCompound data)
	{
		_generic = generic;
		_data = data;
	}
	
	public boolean hasVariable(String name)
	{
		return _generic.hasVariable(name);
	}
	
	public String getName()
	{
		return _generic.getName();
	}
	
	public Set<String> getVarNames()
	{
		return _generic.getVarNames();
	}
	
	public NBTVariable getVariable(String name)
	{
		return _generic.getVariable(name, _data);
	}
	
	@Override
	public Iterator<NBTVariable> iterator()
	{
		return new NBTVariableIterator(_generic._variables, _data);
	}
	
}

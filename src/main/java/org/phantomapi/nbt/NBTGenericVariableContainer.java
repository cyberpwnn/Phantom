package org.phantomapi.nbt;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public final class NBTGenericVariableContainer
{
	
	private String _name;
	private HashMap<String, String> _variableNames;
	LinkedHashMap<String, NBTGenericVariable> _variables;
	
	public NBTGenericVariableContainer(String name)
	{
		_name = name;
		_variableNames = new HashMap<String, String>();
		_variables = new LinkedHashMap<String, NBTGenericVariable>();
	}
	
	public void add(String name, NBTGenericVariable variable)
	{
		String lower = name.toLowerCase();
		if(!_variableNames.containsKey(lower))
		{
			_variableNames.put(lower, name);
			_variables.put(name, variable);
		}
	}
	
	public boolean hasVariable(String name)
	{
		return _variableNames.containsKey(name.toLowerCase());
	}
	
	public String getName()
	{
		return _name;
	}
	
	public Set<String> getVarNames()
	{
		return _variables.keySet();
	}
	
	public NBTVariableContainer boundToData(NBTTagCompound data)
	{
		return new NBTVariableContainer(this, data);
	}
	
	public NBTVariable getVariable(String name, NBTTagCompound data)
	{
		String formalName = _variableNames.get(name.toLowerCase());
		if(formalName != null)
		{
			return new NBTVariable(formalName, _variables.get(formalName), data);
		}
		return null;
	}
	
}

package org.phantomapi.text;

import org.phantomapi.util.F;

/**
 * Adapt to params
 * 
 * @author cyberpwn
 */
public abstract class ParameterAdapter
{
	public ParameterAdapter()
	{
		
	}
	
	public String adapt(String s)
	{
		for(String i : F.getParameters(s, '%'))
		{
			s = s.replaceAll("%" + i + "%", onParameterRequested(i));
		}
		
		return s;
	}
	
	public abstract String onParameterRequested(String parameter);
}

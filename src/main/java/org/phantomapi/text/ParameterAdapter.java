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
	
	/**
	 * A String to adapt with all adapters
	 * 
	 * @param s
	 *            the string
	 * @return the adapted string
	 */
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

package org.phantomapi.registry;

import org.phantomapi.lang.GMap;

/**
 * Registry implementation
 * 
 * @author cyberpwn
 * @param <T>
 *            the type of registry
 */
public class RegistryBank<T> implements Registry<T>
{
	private GMap<String, T> registry;
	
	/**
	 * Create a registry
	 */
	public RegistryBank()
	{
		registry = new GMap<String, T>();
	}
	
	@Override
	public void set(String s, T t)
	{
		registry.put(s, t);
	}
	
	@Override
	public T get(String s)
	{
		return registry.get(s);
	}
	
	@Override
	public boolean contains(String s)
	{
		return registry.containsKey(s);
	}
}

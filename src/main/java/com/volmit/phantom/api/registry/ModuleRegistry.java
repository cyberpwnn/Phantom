package com.volmit.phantom.api.registry;

import com.volmit.phantom.api.lang.GList;

public class ModuleRegistry<T> implements Registry<T>
{
	protected final GList<T> registries;

	public ModuleRegistry()
	{
		registries = new GList<T>();
	}

	@Override
	public void register(T t)
	{
		registries.add(t);
	}

	@Override
	public boolean unregister(T t)
	{
		return registries.remove(t);
	}

	@Override
	public GList<T> getRegistered()
	{
		return registries.copy();
	}

	@Override
	public void unregisterAll()
	{
		for(T i : getRegistered())
		{
			unregister(i);
		}
	}
}

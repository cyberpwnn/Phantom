package com.volmit.phantom.api.registry;

import com.volmit.phantom.api.lang.GList;

public interface Registry<T>
{
	public void register(T t);

	public boolean unregister(T t);

	public GList<T> getRegistered(T t);

	public void unregisterAll();
}

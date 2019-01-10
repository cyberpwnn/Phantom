package com.volmit.phantom.api.lang;

@SuppressWarnings("hiding")
@FunctionalInterface
public interface Resolver<K, V>
{
	public V resolve(K k);
}

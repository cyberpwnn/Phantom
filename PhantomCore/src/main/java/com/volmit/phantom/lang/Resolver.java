package com.volmit.phantom.lang;

@FunctionalInterface
public interface Resolver<K, V>
{
	public V resolve(K k);
}

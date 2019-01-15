package com.volmit.phantom.api.lang;

@SuppressWarnings("hiding")
public class CacheMap<K, V>
{
	private int limit;
	private GList<K> conveyor;
	private GMap<K, V> map;

	public CacheMap(int limit)
	{
		this.limit = limit;
		map = new GMap<K, V>();
		conveyor = new GList<K>();
	}

	public V get(K k)
	{
		return map.get(k);
	}

	public boolean contains(K k)
	{
		return map.containsKey(k);
	}

	public void put(K k, V v)
	{
		map.put(k, v);

		if(!conveyor.contains(k))
		{
			conveyor.add(k);
		}

		while(conveyor.size() > limit)
		{
			map.remove(conveyor.get(conveyor.last()));
			conveyor.remove(conveyor.last());
		}
	}
}

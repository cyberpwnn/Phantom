package org.phantomapi.lang;

/**
 * Upon invoking k() or v(), sorted values will be returned
 * 
 * @author cyberpwn
 * @param <K>
 *            the key
 * @param <V>
 *            the value
 */
public class SortedGMap<K, V> extends GMap<K, V>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Returns a copied AND SORTED list of the keys
	 */
	public GList<K> k()
	{
		GList<V> v = super.v();
		GList<K> k = new GList<K>();
		
		v.sort();
		
		for(V i : v)
		{
			for(K j : super.k())
			{
				if(get(j) == i)
				{
					k.add(j);
				}
			}
		}
		
		return k;
	}
	
	/**
	 * Returns a copied AND SORTED list of the values
	 */
	public GList<V> v()
	{
		GList<K> k = super.k();
		GList<V> v = new GList<V>();
		
		k.sort();
		
		for(K i : k)
		{
			for(V j : super.v())
			{
				if(get(i) == j)
				{
					k.add(i);
				}
			}
		}
		
		return v;
	}
}

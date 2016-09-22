package org.phantomapi.text;

import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.DataCluster.ClusterDataType;
import org.phantomapi.lang.GList;

/**
 * Represents a set of messages used in speech
 * 
 * @author cyberpwn
 */
public class SpeechMesh extends ConfigurableObject
{
	private GList<ParameterAdapter> adapters;
	
	/**
	 * Speech mesh
	 * 
	 * @param id
	 *            the id in the need of saving
	 */
	public SpeechMesh(String id)
	{
		super(id);
		
		this.adapters = new GList<ParameterAdapter>();
	}
	
	private void add(String cat, String value)
	{
		if(!getConfiguration().contains(cat, ClusterDataType.STRING_LIST))
		{
			getConfiguration().set(cat, new GList<String>());
		}
		
		getConfiguration().getStringList(cat).add(value);
	}
	
	private String adapt(String string)
	{
		for(ParameterAdapter i : adapters)
		{
			string = i.adapt(string);
		}
		
		return string;
	}
	
	private GList<String> adapt(GList<String> strings)
	{
		GList<String> s = new GList<String>();
		
		for(String i : strings)
		{
			s.add(adapt(i));
		}
		
		return s;
	}
	
	/**
	 * Add a value to this keyed category
	 * 
	 * @param key
	 *            the category
	 * @param value
	 *            the value(s)
	 */
	public void put(String key, String value)
	{
		add(key, value);
	}
	
	/**
	 * Add a value to this keyed category
	 * 
	 * @param key
	 *            the category
	 * @param value
	 *            the value(s)
	 */
	public void put(String key, GList<String> values)
	{
		for(String i : values)
		{
			put(key, i);
		}
	}
	
	/**
	 * Add a value to this keyed category
	 * 
	 * @param key
	 *            the category
	 * @param value
	 *            the value(s)
	 */
	public void put(String key, String... values)
	{
		for(String i : values)
		{
			put(key, i);
		}
	}
	
	/**
	 * Get all strings within this category. This automatically fires off any
	 * parameter adapters if needed
	 * 
	 * @param category
	 *            the category
	 * @return a list of strings or null
	 */
	public GList<String> getAll(String category)
	{
		if(!getConfiguration().contains(category, ClusterDataType.STRING_LIST))
		{
			return null;
		}
		
		return adapt(new GList<String>(getConfiguration().getStringList(category)));
	}
	
	/**
	 * Get a random string from the category This automatically fires off any
	 * parameter adapters if needed
	 * 
	 * @param category
	 *            the category
	 * @return the result or null if non existant category
	 */
	public String get(String category)
	{
		if(!getConfiguration().contains(category, ClusterDataType.STRING_LIST))
		{
			return null;
		}
		
		return getAll(category).pickRandom();
	}
	
	/**
	 * Add a parameter adapter
	 * 
	 * @param adapter
	 *            the adapter
	 */
	public void add(ParameterAdapter adapter)
	{
		adapters.add(adapters);
	}
	
	/**
	 * Clear all adapters
	 */
	public void clearAdapters()
	{
		adapters.clear();
	}
	
	/**
	 * Remove parameter adapter
	 * 
	 * @param adapter
	 *            the adapter
	 */
	public void remove(ParameterAdapter adapter)
	{
		adapters.remove(adapter);
	}
}

package org.cyberpwn.phantom.clust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The data cluster holds keyed values in paths ready to be written to files in
 * different ways
 * 
 * @author cyberpwn
 *
 */
public class DataCluster
{
	/**
	 * 
	 * @author cyberpwn
	 *
	 */
	public enum ClusterDataType
	{
		INTEGER, DOUBLE, BOOLEAN, STRING, STRING_LIST;
	}
	
	private Map<String, Cluster> data;
	
	/**
	 * Initializes a new data cluster
	 */
	public DataCluster()
	{
		this.data = new HashMap<String, Cluster>();
	}
	
	/**
	 * Attempt to set a value to a path. Make sure it is a compatible type
	 * 
	 * @param key
	 *            the path
	 * @param o
	 *            the object
	 */
	public void trySet(String key, Object o)
	{
		if(o == null)
		{
			return;
		}
		
		if(o instanceof Integer)
		{
			set(key, (Integer) o);
		}
		
		else if(o instanceof String)
		{
			set(key, (String) o);
		}
		
		else if(o instanceof Double)
		{
			set(key, (Double) o);
		}
		
		else if(o instanceof Boolean)
		{
			set(key, (Boolean) o);
		}
		
		else if(o instanceof List)
		{
			List<String> l = new ArrayList<String>();
			
			for(Object i : (List<?>) o)
			{
				l.add(i.toString());
			}
			
			set(key, l);
		}
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, int value)
	{
		data.put(key, new ClusterInteger(value));
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, double value)
	{
		data.put(key, new ClusterDouble(value));
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, boolean value)
	{
		data.put(key, new ClusterBoolean(value));
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, String value)
	{
		data.put(key, new ClusterString(value));
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, List<String> value)
	{
		data.put(key, new ClusterStringList(value));
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public Boolean getBoolean(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.BOOLEAN))
		{
			return ((ClusterBoolean) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public Integer getInt(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.INTEGER))
		{
			return ((ClusterInteger) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public Double getDouble(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.DOUBLE))
		{
			return ((ClusterDouble) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public String getString(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.STRING))
		{
			return ((ClusterString) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public List<String> getStringList(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.STRING_LIST))
		{
			return ((ClusterStringList) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Check if the cluster contains the given key
	 * 
	 * @param key
	 *            the given key
	 * @return true if key exists, false if no such key
	 */
	public boolean contains(String key)
	{
		return data.containsKey(key) && data.get(key) != null;
	}
	
	/**
	 * Remove the key and assigned object from the cluster
	 * 
	 * @param key
	 *            the given key
	 */
	public void remove(String key)
	{
		data.remove(key);
	}
	
	/**
	 * Get an object from the key, does not require a known type for getting it.
	 * 
	 * @param key
	 *            the given key
	 * @return the object
	 */
	public Object getAbstract(String key)
	{
		if(getType(key).equals(ClusterDataType.BOOLEAN))
		{
			return getBoolean(key);
		}
		
		else if(getType(key).equals(ClusterDataType.INTEGER))
		{
			return getInt(key);
		}
		
		else if(getType(key).equals(ClusterDataType.DOUBLE))
		{
			return getDouble(key);
		}
		
		else if(getType(key).equals(ClusterDataType.STRING))
		{
			return getString(key);
		}
		
		else if(getType(key).equals(ClusterDataType.STRING_LIST))
		{
			return getStringList(key);
		}
		
		return null;
	}
	
	/**
	 * Get the raw cluster object for the key
	 * 
	 * @param key
	 *            the given key
	 * @return the cluster object
	 */
	public Cluster get(String key)
	{
		return data.get(key);
	}
	
	/**
	 * Get the cluster type for the given key
	 * 
	 * @param key
	 *            the key
	 * @return the type
	 */
	public ClusterDataType getType(String key)
	{
		return get(key).getType();
	}
	
	/**
	 * Get the cluster set
	 * 
	 * @return the map
	 */
	public Map<String, Cluster> getData()
	{
		return data;
	}
	
	/**
	 * Set the data
	 * 
	 * @param data
	 *            the data
	 */
	public void setData(Map<String, Cluster> data)
	{
		this.data = data;
	}
}

package org.cyberpwn.phantom.clust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCluster
{
	public enum ClusterDataType
	{
		INTEGER, DOUBLE, BOOLEAN, STRING, STRING_LIST;
	}
	
	private Map<String, Cluster> data;
	
	public DataCluster()
	{
		this.data = new HashMap<String, Cluster>();
	}
	
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
			
			for(Object i : (List<?>)o)
			{
				l.add(i.toString());
			}
			
			set(key, l);
		}
	}
	
	public void set(String key, int value)
	{
		data.put(key, new ClusterInteger(value));
	}
	
	public void set(String key, double value)
	{
		data.put(key, new ClusterDouble(value));
	}
	
	public void set(String key, boolean value)
	{
		data.put(key, new ClusterBoolean(value));
	}
	
	public void set(String key, String value)
	{
		data.put(key, new ClusterString(value));
	}
	
	public void set(String key, List<String> value)
	{
		data.put(key, new ClusterStringList(value));
	}
	
	public Boolean getBoolean(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.BOOLEAN))
		{
			return ((ClusterBoolean) get(key)).get();
		}
		
		return null;
	}
	
	public Integer getInt(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.INTEGER))
		{
			return ((ClusterInteger) get(key)).get();
		}
		
		return null;
	}
	
	public Double getDouble(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.DOUBLE))
		{
			return ((ClusterDouble) get(key)).get();
		}
		
		return null;
	}
	
	public String getString(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.STRING))
		{
			return ((ClusterString) get(key)).get();
		}
		
		return null;
	}
	
	public List<String> getStringList(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.STRING_LIST))
		{
			return ((ClusterStringList) get(key)).get();
		}
		
		return null;
	}
	
	public boolean contains(String key)
	{
		return data.containsKey(key) && data.get(key) != null;
	}
	
	public void remove(String key)
	{
		data.remove(key);
	}
	
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
	
	public Cluster get(String key)
	{
		return data.get(key);
	}
	
	public ClusterDataType getType(String key)
	{
		return get(key).getType();
	}
	
	public Map<String, Cluster> getData()
	{
		return data;
	}
	
	public void setData(Map<String, Cluster> data)
	{
		this.data = data;
	}
}

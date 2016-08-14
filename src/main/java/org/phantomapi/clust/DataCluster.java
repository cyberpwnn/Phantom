package org.phantomapi.clust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.phantomapi.util.Paste;

/**
 * The data cluster holds keyed values in paths ready to be written to files in
 * different ways
 * 
 * @author cyberpwn
 */
public class DataCluster
{
	/**
	 * @author cyberpwn
	 */
	public enum ClusterDataType
	{
		INTEGER, DOUBLE, BOOLEAN, STRING, STRING_LIST, LONG;
	}
	
	private Map<String, Cluster> data;
	private Map<String, String> comments;
	
	/**
	 * Initializes a new data cluster
	 */
	public DataCluster()
	{
		this.data = new HashMap<String, Cluster>();
		this.comments = new HashMap<String, String>();
	}
	
	/**
	 * Pastes the yml to paste.phantomapi.org/
	 * 
	 * @return the url to access it
	 */
	public String paste()
	{
		try
		{
			return Paste.paste(toYaml().saveToString());
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Initializes a new data cluster
	 */
	public DataCluster(Map<String, Cluster> data)
	{
		this.data = data;
		this.comments = new HashMap<String, String>();
	}
	
	/**
	 * Add a comment to the key
	 * 
	 * @param key
	 *            the key
	 * @param comment
	 *            the comment
	 */
	public void comment(String key, String comment)
	{
		this.comments.put(key, comment);
	}
	
	/**
	 * Output all data to a json object
	 * 
	 * @return the json object
	 */
	public JSONObject toJSON()
	{
		JSONObject jso = new JSONObject();
		
		for(String i : getData().keySet())
		{
			jso.put(i, getAbstract(i));
		}
		
		return jso;
	}
	
	/**
	 * Add the data within the json object to the datacluster
	 * 
	 * @param jso
	 */
	public void addJson(JSONObject jso)
	{
		for(String i : jso.keySet())
		{
			trySet(i, jso.get(i));
		}
	}
	
	/**
	 * Clears the data in this datacluster
	 */
	public void clear()
	{
		data.clear();
	}
	
	/**
	 * Has Comment?
	 * 
	 * @param key
	 *            the key
	 * @return true if the comment exists
	 */
	public boolean hasComment(String key)
	{
		return comments.containsKey(key);
	}
	
	/**
	 * Get the comment
	 * 
	 * @param key
	 *            the key
	 * @return the comment, or null
	 */
	public String getComment(String key)
	{
		return comments.get(key);
	}
	
	/**
	 * Get the comment in a list split from newline breaks
	 * 
	 * @param key
	 *            the key
	 * @return the list of comments, or empty if no comments
	 */
	public List<String> getComments(String key)
	{
		List<String> comments = new ArrayList<String>();
		
		if(hasComment(key))
		{
			if(getComment(key).contains("\n"))
			{
				for(String i : getComment(key).split("\n"))
				{
					comments.add(i);
				}
			}
			
			else
			{
				comments.add(getComment(key));
			}
		}
		
		return comments;
	}
	
	/**
	 * Get the lines in yml format
	 * 
	 * @param comment
	 *            should we add comments?
	 * @return the list of lines
	 */
	public List<String> toLines(boolean comment)
	{
		FileConfiguration fc = toYaml();
		List<String> lines = new ArrayList<String>();
		List<String> main = new ArrayList<String>();
		
		for(String i : fc.saveToString().split("\n"))
		{
			main.add(i);
		}
		
		for(String i : main)
		{
			String key = i.split(": ")[0].replaceAll(" ", "");
			
			for(String j : fc.getKeys(true))
			{
				if(j.endsWith("." + key))
				{
					if(comment)
					{
						if(hasComment(j))
						{
							lines.add(" ");
							
							for(String k : getComments(j))
							{
								int kx = i.split(": ")[0].split(" ").length - 1;
								lines.add(StringUtils.repeat(" ", kx) + "# " + k);
							}
						}
					}
				}
			}
			
			lines.add(i);
		}
		
		return lines;
	}
	
	/**
	 * Get a FileConfiguration object from this cluster
	 * 
	 * @return the yml object
	 */
	public FileConfiguration toYaml()
	{
		FileConfiguration fc = new YamlConfiguration();
		
		for(String i : data.keySet())
		{
			fc.set(i, getAbstract(i));
		}
		
		return fc;
	}
	
	/**
	 * Attempt to set a value to a path. Make sure it is a compatible type
	 * 
	 * @param key
	 *            the path
	 * @param o
	 *            the object
	 * @param comment
	 *            the comment
	 */
	public void trySet(String key, Object o, String comment)
	{
		trySet(key, o);
		comment(key, comment);
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
		
		if(o instanceof Long)
		{
			set(key, (Long) o);
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
		
		else if(o instanceof JSONArray)
		{
			List<String> l = new ArrayList<String>();
			
			for(Object i : ((JSONArray) o))
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
	public void set(String key, long value)
	{
		data.put(key, new ClusterLong(value));
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
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, int value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, long value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, double value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, boolean value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, String value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, List<String> value, String comment)
	{
		set(key, value);
		comment(key, comment);
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
	public Long getLong(String key)
	{
		if(contains(key) && getType(key).equals(ClusterDataType.LONG))
		{
			return ((ClusterLong) get(key)).get();
		}
		
		if(contains(key) && getType(key).equals(ClusterDataType.INTEGER))
		{
			return (long) ((ClusterInteger) get(key)).get();
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
		
		else if(getType(key).equals(ClusterDataType.LONG))
		{
			return getLong(key);
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

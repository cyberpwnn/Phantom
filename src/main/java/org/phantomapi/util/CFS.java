package org.phantomapi.util;

import org.phantomapi.Phantom;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataCluster.ClusterDataType;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GList;

/**
 * Control the Cluster File System
 * 
 * @author cyberpwn
 */
public class CFS
{
	/**
	 * Set a datacluster key
	 * 
	 * @param c
	 *            the controller
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public static void set(Controllable c, String key, String value)
	{
		getConfiguration(c).trySet(key, getValueForType(value, getType(c, key)));
	}
	
	/**
	 * Get a datacluster key
	 * 
	 * @param c
	 *            the controller
	 * @param key
	 *            the key
	 * @return the value
	 */
	public static String get(Controllable c, String key)
	{
		return getConfiguration(c).getAbstract(key).toString();
	}
	
	/**
	 * Get the node id
	 * 
	 * @param c
	 *            the controller
	 * @param key
	 *            the key
	 * @return the node string
	 */
	public static String getNode(Controllable c, String key)
	{
		return C.DARK_GRAY + c.getName() + C.LIGHT_PURPLE + "/" + C.DARK_GRAY + key + C.LIGHT_PURPLE + ": (" + getType(c, key).toString().toLowerCase().replaceAll("_", " ") + ") " + get(c, key);
	}
	
	/**
	 * Get a value from the given type
	 * 
	 * @param v
	 *            the value
	 * @param t
	 *            the type
	 * @return the object
	 */
	public static Object getValueForType(String v, ClusterDataType t)
	{
		switch(t)
		{
			case BOOLEAN:
				return Boolean.valueOf(v);
			case DOUBLE:
				return Double.valueOf(v);
			case INTEGER:
				return Integer.valueOf(v);
			case LONG:
				return Long.valueOf(v);
			case STRING:
				return v;
			default:
				break;
		}
		
		return null;
	}
	
	/**
	 * Get all configurable controllers
	 * 
	 * @return the controllers
	 */
	public static GList<Controllable> getConfigurableControllers()
	{
		GList<Controllable> con = new GList<Controllable>();
		
		for(Controllable i : Phantom.instance().getBindings())
		{
			if(i instanceof Configurable && ((Configurable) i).getConfiguration() != null)
			{
				con.add(i);
			}
		}
		
		return con;
	}
	
	/**
	 * Get the data cluster for this controller
	 * 
	 * @param c
	 *            the controller
	 * @return the data cluster
	 */
	public static DataCluster getConfiguration(Controllable c)
	{
		return ((Configurable) c).getConfiguration();
	}
	
	/**
	 * Get the keys in the data cluster
	 * 
	 * @param c
	 *            the controller
	 * @return the keys
	 */
	public static GList<String> getKeys(Controllable c)
	{
		return new GList<String>(getConfiguration(c).getData().keySet());
	}
	
	/**
	 * Get the datacluster type
	 * 
	 * @param c
	 *            the controllable
	 * @param k
	 *            the key
	 * @return the type
	 */
	public static ClusterDataType getType(Controllable c, String k)
	{
		return getConfiguration(c).getType(k);
	}
	
	/**
	 * Does the controller and key exist?
	 * 
	 * @param c
	 *            the controller
	 * @param k
	 *            the key
	 * @return true if it does
	 */
	public static boolean exists(String c, String k)
	{
		return exists(c) && getConfiguration(getController(c)).contains(k);
	}
	
	/**
	 * Does the controller and typed key exist?
	 * 
	 * @param c
	 *            the controller
	 * @param k
	 *            the key
	 * @param t
	 *            the type
	 * @return true if it does
	 */
	public static boolean exists(String c, String k, ClusterDataType t)
	{
		return exists(c) && getConfiguration(getController(c)).contains(k, t);
	}
	
	/**
	 * Get the controller for string
	 * 
	 * @param c
	 *            the string
	 * @return the controller or null
	 */
	public static Controllable getController(String c)
	{
		for(Controllable i : getConfigurableControllers())
		{
			if(i.getName().equalsIgnoreCase(c))
			{
				return i;
			}
		}
		
		for(Controllable i : getConfigurableControllers())
		{
			if(i.getName().toLowerCase().contains(c.toLowerCase()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	/**
	 * Does the controller exist
	 * 
	 * @param c
	 *            the controller
	 * @return true if it does
	 */
	public static boolean exists(String c)
	{
		return getController(c) != null;
	}
}

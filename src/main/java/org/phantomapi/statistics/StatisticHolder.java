package org.phantomapi.statistics;

import org.phantomapi.Phantom;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataCluster.ClusterDataType;
import org.phantomapi.lang.GMap;

/**
 * A Stat holder
 * 
 * @author cyberpwn
 */
public class StatisticHolder implements Configurable
{
	private DataCluster cc;
	private Analytical<?> parent;
	
	/**
	 * Create a new stat holder
	 * 
	 * @param parent
	 *            the analytical object which binds these statistics
	 */
	public StatisticHolder(Analytical<?> parent)
	{
		cc = new DataCluster();
		this.parent = parent;
	}
	
	/**
	 * Get the analytical object which holds these statistics
	 * 
	 * @return the parent
	 */
	public Analytical<?> getParent()
	{
		return parent;
	}
	
	@Override
	public void onNewConfig()
	{
		
	}
	
	@Override
	public void onReadConfig()
	{
		
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	/**
	 * Must be called from a sub class from this class, as this class is
	 * abstract. Define a Tabled annotation above the subclass
	 */
	public void toSql()
	{
		Phantom.instance().saveMysql(this);
	}
	
	/**
	 * Must be called from a sub class from this class, as this class is
	 * abstract. Define a Tabled annotation above the subclass
	 */
	public void fromSql()
	{
		Phantom.instance().loadMysql(this);
	}
	
	@Override
	public String getCodeName()
	{
		return parent.getCodeName();
	}
	
	/**
	 * Set a key value pair
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value in double form
	 */
	public void set(String key, double value)
	{
		cc.set(key, value);
	}
	
	/**
	 * Get a value from a key
	 * 
	 * @param key
	 *            the key
	 * @return the double, null if non exist or invalid type
	 */
	public Double get(String key)
	{
		return cc.getDouble(key);
	}
	
	/**
	 * Does this holder have a key in stats
	 * 
	 * @param key
	 *            the key
	 * @return true if the stat exists
	 */
	public boolean has(String key)
	{
		return cc.contains(key);
	}
	
	/**
	 * Add to a stat. If the stat exists, it will be added to the stat. If it
	 * does not exist, it will be created with the value (0 + value)
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void add(String key, double value)
	{
		if(has(key))
		{
			if(cc.getType(key).equals(ClusterDataType.DOUBLE))
			{
				set(key, get(key) + value);
			}
		}
		
		else
		{
			set(key, value);
		}
	}
	
	/**
	 * Get all valid stats
	 * 
	 * @return the stats in a hashmap
	 */
	public GMap<String, Double> getStatistics()
	{
		GMap<String, Double> map = new GMap<String, Double>();
		
		for(String i : cc.getData().keySet())
		{
			if(cc.getType(i).equals(ClusterDataType.DOUBLE))
			{
				map.put(i, get(i));
			}
		}
		
		return map;
	}
}

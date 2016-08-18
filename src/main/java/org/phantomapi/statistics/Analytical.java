package org.phantomapi.statistics;

import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataCluster.ClusterDataType;

/**
 * Analytical statistic objects
 * 
 * @author cyberpwn
 */
public abstract class Analytical implements Configurable
{
	private DataCluster cc;
	
	/**
	 * Make an analytical object (super)
	 */
	public Analytical()
	{
		this.cc = new DataCluster();
	}
	
	@Override
	public abstract void onNewConfig();
	
	@Override
	public abstract void onReadConfig();
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public abstract String getCodeName();
	
	/**
	 * Set a stat
	 * 
	 * @param s
	 *            the key
	 * @param d
	 *            the data
	 */
	public void set(String s, Double d)
	{
		cc.set(s, (Double) d);
	}
	
	/**
	 * Get a stat or 0 if null
	 * 
	 * @param s
	 *            the stat key
	 * @return the value
	 */
	public Double get(String s)
	{
		if(!has(s))
		{
			return 0.0;
		}
		
		if(cc.getType(s).equals(ClusterDataType.DOUBLE))
		{
			return cc.getDouble(s);
		}
		
		if(cc.getType(s).equals(ClusterDataType.INTEGER))
		{
			return cc.getInt(s).doubleValue();
		}
		
		return 0.0;
	}
	
	/**
	 * Does it have the stat in double form?
	 * 
	 * @param s
	 *            the stat
	 * @return true if it does
	 */
	public Boolean has(String s)
	{
		return cc.contains(s);
	}
	
	/**
	 * Add data to the stat
	 * 
	 * @param s
	 *            the stat
	 * @param inc
	 *            the increment
	 */
	public void add(String s, Double inc)
	{
		if(!has(s))
		{
			set(s, inc);
		}
		
		else
		{
			set(s, get(s) + inc);
		}
	}
	
	/**
	 * Add 1 to the stat
	 * 
	 * @param s
	 *            the stat
	 */
	public void add(String s)
	{
		add(s, 1.0);
	}
}

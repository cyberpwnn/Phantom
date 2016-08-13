package org.phantomapi.util;

import org.phantomapi.lang.GList;

/**
 * An average object
 * 
 * @author cyberpwn
 *
 */
public class Average
{
	private int limit;
	private GList<Double> data;
	private double average;
	
	/**
	 * Create an average with a limit to max data
	 * 
	 * @param limit
	 *            the limit
	 */
	public Average(int limit)
	{
		this.limit = limit;
		this.data = new GList<Double>();
		this.average = 0.0;
	}
	
	/**
	 * Puts a new data entry into the average, and trims the oldest data if it
	 * exceeds the limit
	 * 
	 * @param d
	 *            the data
	 */
	public void put(double d)
	{
		data.add(d);
		
		if(limit > 0)
		{
			M.lim(data, limit);
		}
		
		average = M.avg(data);
	}
	
	/**
	 * Get the limit
	 * 
	 * @return the limit
	 */
	public int getLimit()
	{
		return limit;
	}
	
	/**
	 * Set the limit
	 * 
	 * @param limit
	 *            the limit
	 */
	public void setLimit(int limit)
	{
		this.limit = limit;
	}
	
	/**
	 * Get the data
	 * 
	 * @return the data
	 */
	public GList<Double> getData()
	{
		return data;
	}
	
	/**
	 * Set the data
	 * 
	 * @param data
	 *            the data
	 */
	public void setData(GList<Double> data)
	{
		this.data = data;
	}
	
	/**
	 * Get the average
	 * 
	 * @return the average
	 */
	public double getAverage()
	{
		return average;
	}
}

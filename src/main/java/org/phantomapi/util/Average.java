package org.phantomapi.util;

import org.phantomapi.lang.GList;

/**
 * An average object
 * 
 * @author cyberpwn
 */
public class Average
{
	private int limit;
	private GList<Double> data;
	private double average;
	
	/**
	 * Average the list by looping through them and mapping an average with a
	 * limit
	 * 
	 * @param doubles
	 *            the doubles
	 * @param limit
	 *            the limit
	 * @return the doubles
	 */
	public static GList<Double> getFastAverage(GList<Double> doubles, int limit)
	{
		GList<Double> d = new GList<Double>();
		Average a = new Average(limit);
		
		for(Double i : doubles)
		{
			a.put(i);
			d.add(a.getAverage());
		}
		
		return d;
	}
	
	/**
	 * Create an average with a limit to max data
	 * 
	 * @param limit
	 *            the limit
	 */
	public Average(int limit)
	{
		this.limit = limit;
		data = new GList<Double>();
		average = 0.0;
	}
	
	/**
	 * Reset the stored data and cached average
	 */
	public void reset()
	{
		data.clear();
		average = 0.0;
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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(average);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + limit;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		Average other = (Average) obj;
		
		if(Double.doubleToLongBits(average) != Double.doubleToLongBits(other.average))
		{
			return false;
		}
		
		if(data == null)
		{
			if(other.data != null)
			{
				return false;
			}
		}
		
		else if(!data.equals(other.data))
		{
			return false;
		}
		
		if(limit != other.limit)
		{
			return false;
		}
		
		return true;
	}
}

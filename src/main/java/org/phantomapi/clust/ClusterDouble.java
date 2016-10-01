package org.phantomapi.clust;

import org.phantomapi.clust.DataCluster.ClusterDataType;

/**
 * 
 * @author cyberpwn
 *
 */
public class ClusterDouble extends Cluster
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ClusterDouble(Double value)
	{
		super(ClusterDataType.DOUBLE, value);
	}
	
	public double get()
	{
		return value.doubleValue();
	}
	
	public void set(double i)
	{
		value = i;
	}
}

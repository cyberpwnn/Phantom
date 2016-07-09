package org.cyberpwn.phantom.clust;

import org.cyberpwn.phantom.clust.DataCluster.ClusterDataType;

public class ClusterDouble extends Cluster
{
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

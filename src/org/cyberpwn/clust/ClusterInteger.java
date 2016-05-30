package org.cyberpwn.clust;

import org.cyberpwn.clust.DataCluster.ClusterDataType;

public class ClusterInteger extends Cluster
{
	public ClusterInteger(Integer value)
	{
		super(ClusterDataType.INTEGER, value.doubleValue());
	}
	
	public int get()
	{
		return value.intValue();
	}
	
	public void set(int i)
	{
		value = (double) i;
	}
}

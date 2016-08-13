package org.phantomapi.clust;

import org.phantomapi.clust.DataCluster.ClusterDataType;

/**
 * 
 * @author cyberpwn
 *
 */
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

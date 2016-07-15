package org.cyberpwn.phantom.clust;

import org.cyberpwn.phantom.clust.DataCluster.ClusterDataType;

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

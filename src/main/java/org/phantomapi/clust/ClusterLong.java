package org.phantomapi.clust;

import org.phantomapi.clust.DataCluster.ClusterDataType;

/**
 * 
 * @author cyberpwn
 *
 */
public class ClusterLong extends Cluster
{
	public ClusterLong(Long value)
	{
		super(ClusterDataType.LONG, (double) value);
	}
	
	public long get()
	{
		return value.longValue();
	}
	
	public void set(long i)
	{
		value = (double) i;
	}
}

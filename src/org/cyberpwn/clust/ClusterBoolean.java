package org.cyberpwn.clust;

import org.cyberpwn.clust.DataCluster.ClusterDataType;

public class ClusterBoolean extends Cluster
{
	public ClusterBoolean(boolean value)
	{
		super(ClusterDataType.BOOLEAN, (double) (value ? 1 : 0));
	}
	
	public boolean get()
	{
		return value == 1;
	}
	
	public void set(boolean b)
	{
		value = (double) (b ? 1 : 0);
	}
}

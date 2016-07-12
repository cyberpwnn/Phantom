package org.cyberpwn.phantom.clust;

import org.cyberpwn.phantom.clust.DataCluster.ClusterDataType;

public class Cluster
{
	protected final ClusterDataType type;
	protected Double value;
	
	public Cluster(ClusterDataType type, Double value)
	{
		this.type = type;
		this.value = value;
	}

	public ClusterDataType getType()
	{
		return type;
	}
}

package org.cyberpwn.clust;

import org.cyberpwn.clust.DataCluster.ClusterDataType;

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

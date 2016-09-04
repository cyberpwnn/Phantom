package org.phantomapi.clust;

import org.phantomapi.clust.DataCluster.ClusterDataType;

/**
 * 
 * @author cyberpwn
 *
 */
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
	
	public boolean equals(Object o)
	{
		if(o instanceof Cluster)
		{
			return ((Cluster)o).getType().equals(getType());
		}
		
		return false;
	}
}

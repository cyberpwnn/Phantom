package org.cyberpwn.clust;

import org.cyberpwn.clust.DataCluster.ClusterDataType;

public class ClusterString extends Cluster
{
	private String string;
	
	public ClusterString(String value)
	{
		super(ClusterDataType.STRING, 0.0);
		this.string = value;
	}
	
	public String get()
	{
		return string;
	}
	
	public void set(String s)
	{
		value = 0.0;
		string = s;
	}
}

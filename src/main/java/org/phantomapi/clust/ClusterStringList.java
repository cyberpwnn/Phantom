package org.phantomapi.clust;

import java.util.List;
import org.phantomapi.clust.DataCluster.ClusterDataType;

/**
 * 
 * @author cyberpwn
 *
 */
public class ClusterStringList extends Cluster
{
	private List<String> strings;
	
	public ClusterStringList(List<String> value)
	{
		super(ClusterDataType.STRING_LIST, 0.0);
		this.strings = value;
	}
	
	public List<String> get()
	{
		return strings;
	}
	
	public void set(List<String> s)
	{
		value = 0.0;
		strings = s;
	}
}

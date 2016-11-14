package org.phantomapi.blockmeta;

import org.phantomapi.clust.LinkedDataCluster;

/**
 * Represents a configurable object
 * 
 * @author cyberpwn
 */
public class BlockMeta
{
	private LinkedDataCluster cc;
	
	public BlockMeta(LinkedDataCluster cc)
	{
		this.cc = cc;
	}
	
	public LinkedDataCluster getConfiguration()
	{
		return cc;
	}
}

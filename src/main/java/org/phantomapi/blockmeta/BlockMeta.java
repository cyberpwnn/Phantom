package org.phantomapi.blockmeta;

import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.LinkedDataCluster;

/**
 * Represents block meta which hold a data cluster
 * 
 * @author cyberpwn
 */
public class BlockMeta implements Configurable
{
	private LinkedDataCluster cc;
	private String codeName;
	
	/**
	 * Create a block meta
	 * 
	 * @param codeName
	 *            the code name
	 * @param cc
	 *            the linked cluster from a host (use the host to get this
	 *            instance)
	 */
	public BlockMeta(String codeName, LinkedDataCluster cc)
	{
		this.cc = cc;
		this.codeName = codeName;
	}
	
	@Override
	public void onNewConfig()
	{
		
	}
	
	@Override
	public void onReadConfig()
	{
		
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return codeName;
	}
	
	/**
	 * Write changes back to the parent
	 */
	public void write()
	{
		cc.flushParent();
	}
}

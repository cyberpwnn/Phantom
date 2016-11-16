package org.phantomapi.blockmeta;

import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.LinkedDataCluster;

public class BlockMeta implements Configurable
{
	private LinkedDataCluster cc;
	private String codeName;
	
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
	
	public void write()
	{
		cc.flushParent();
	}
}

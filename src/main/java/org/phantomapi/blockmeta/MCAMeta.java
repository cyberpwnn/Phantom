package org.phantomapi.blockmeta;

import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.LinkedDataCluster;
import org.phantomapi.lang.GChunk;
import org.phantomapi.lang.GMCA;

public class MCAMeta implements Configurable
{
	private String world;
	private int x;
	private int z;
	private LinkedDataCluster cc;
	private String codeName;
	
	public MCAMeta(GMCA gmca, LinkedDataCluster cc)
	{
		world = gmca.getWorld();
		x = gmca.getX();
		z = gmca.getZ();
		this.cc = cc;
		codeName = x + "." + z;
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
	
	public boolean hasData()
	{
		return getConfiguration().keys().size() > 1;
	}
	
	public ChunkMeta getChunk(GChunk chunk)
	{
		if(chunk.getX() >> 5 == x && chunk.getZ() >> 5 == z && chunk.getWorld().equals(world))
		{
			getConfiguration().flushLinks();
			String cn = chunk.getX() + "-" + chunk.getZ();
			
			if(!getConfiguration().contains("chunks." + cn + ".c-" + cn))
			{
				getConfiguration().set("chunks." + cn + ".c-" + cn, true);
			}
			
			return new ChunkMeta(chunk, getConfiguration().linkSplit("chunks." + cn));
		}
		
		return null;
	}
}

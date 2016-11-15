package org.phantomapi.blockmeta;

import org.bukkit.block.Block;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.LinkedDataCluster;
import org.phantomapi.lang.GChunk;

public class ChunkMeta implements Configurable
{
	private GChunk gc;
	private LinkedDataCluster cc;
	private String codeName;
	
	public ChunkMeta(GChunk gc, LinkedDataCluster cc)
	{
		this.gc = gc;
		this.cc = cc;
		codeName = gc.getX() + "-" + gc.getZ();
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
	
	public BlockMeta getBlock(Block block)
	{
		if(gc.getWorld().equals(block.getWorld().getName()) && gc.getX() == block.getChunk().getX() && gc.getZ() == block.getChunk().getZ())
		{
			String cn = block.getX() + "-" + block.getY() + "-" + block.getZ();
			getConfiguration().flushLinks();
			
			if(!getConfiguration().contains("blocks." + cn + ".b-" + cn))
			{
				getConfiguration().set("blocks." + cn + ".b-" + cn, true);
			}
			
			return new BlockMeta(cn, getConfiguration().linkSplit("blocks." + cn));
		}
		
		return null;
	}
}

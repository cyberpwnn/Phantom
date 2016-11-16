package org.phantomapi.blockmeta;

import java.io.File;
import org.bukkit.block.Block;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.lang.GChunk;
import org.phantomapi.util.Worlds;

public class ChunkMeta implements Configurable
{
	private GChunk gc;
	private DataCluster cc;
	private String codeName;
	
	public ChunkMeta(GChunk gc)
	{
		this.gc = gc;
		cc = new DataCluster();
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
	
	public File getFile()
	{
		return new File(new File(Worlds.getWorld(gc.getWorld()).getWorldFolder(), "meta"), "r." + gc.getX() + "." + gc.getZ() + ".pma");
	}
	
	public boolean hasData()
	{
		return !getConfiguration().keys().isEmpty();
	}
	
	public BlockMeta getBlock(Block block)
	{
		if(gc.getWorld().equals(block.getWorld().getName()) && gc.getX() == block.getChunk().getX() && gc.getZ() == block.getChunk().getZ())
		{
			String cn = block.getX() + "-" + block.getY() + "-" + block.getZ();
			getConfiguration().flushLinks();
			
			return new BlockMeta(cn, getConfiguration().linkSplit("blocks." + cn));
		}
		
		return null;
	}
}

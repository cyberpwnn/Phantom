package org.phantomapi.blockmeta;

import org.bukkit.util.Vector;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.lang.GList;

public class SchematicMeta implements Configurable
{
	private DataCluster cc;
	private String codeName;
	
	public SchematicMeta()
	{
		cc = new DataCluster();
		codeName = "schematic-meta";
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
		return !getConfiguration().keys().isEmpty();
	}
	
	public GList<Vector> getBlocks()
	{
		GList<String> roots = new GList<String>(getConfiguration().getRoots("blocks"));
		GList<Vector> blocks = new GList<Vector>();
		
		for(String i : roots)
		{
			blocks.add(new Vector(Integer.valueOf(i.split("-")[0]), Integer.valueOf(i.split("-")[1]), Integer.valueOf(i.split("-")[2])));
		}
		
		return blocks;
	}
	
	public BlockMeta getBlock(Vector block)
	{
		String cn = block.getBlockX() + "-" + block.getBlockY() + "-" + block.getBlockZ();
		getConfiguration().flushLinks();
		
		return new BlockMeta(cn, getConfiguration().linkSplit("blocks." + cn));
	}
}

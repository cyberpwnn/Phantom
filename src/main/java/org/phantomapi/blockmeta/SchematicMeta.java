package org.phantomapi.blockmeta;

import org.bukkit.util.Vector;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.lang.GList;

/**
 * Represents a meta object which is not bound to a chunk
 * 
 * @author cyberpwn
 */
public class SchematicMeta implements Configurable
{
	private DataCluster cc;
	private String codeName;
	
	/**
	 * Create a schematic meta instance
	 */
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
	
	/**
	 * Does this instance contain any changed data or any data to save at all?
	 * 
	 * @return true if it does
	 */
	public boolean hasData()
	{
		return !getConfiguration().keys().isEmpty();
	}
	
	/**
	 * Get all the vector blocks in this schematic. Since they are relative
	 * vectors are returned instead of blocks
	 * 
	 * @return the vector list
	 */
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
	
	/**
	 * Get a block meta object from a relative vector. If it doesnt exist, it
	 * will be linked and added, then returned for editing.
	 * 
	 * @param block
	 *            the vector
	 * @return the block meta
	 */
	public BlockMeta getBlock(Vector block)
	{
		String cn = block.getBlockX() + "-" + block.getBlockY() + "-" + block.getBlockZ();
		getConfiguration().flushLinks();
		
		return new BlockMeta(cn, getConfiguration().linkSplit("blocks." + cn));
	}
}

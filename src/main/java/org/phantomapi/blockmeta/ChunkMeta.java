package org.phantomapi.blockmeta;

import java.io.File;
import org.bukkit.block.Block;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.lang.GChunk;
import org.phantomapi.lang.GList;
import org.phantomapi.util.Worlds;

/**
 * Represents a meta host which holds meta blocks
 * 
 * @author cyberpwn
 */
public class ChunkMeta implements Configurable
{
	private GChunk gc;
	private DataCluster cc;
	private String codeName;
	
	/**
	 * Create a chunk meta object
	 * 
	 * @param gc
	 *            the gchunk
	 */
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
	
	/**
	 * Get the file name this would be saved to
	 * 
	 * @return the file
	 */
	public File getFile()
	{
		return new File(new File(Worlds.getWorld(gc.getWorld()).getWorldFolder(), "meta"), "r." + gc.getX() + "." + gc.getZ() + ".pma");
	}
	
	/**
	 * Does this chunk have any data other than defaults?
	 * 
	 * @return true if it has data which would be lost if not saved
	 */
	public boolean hasData()
	{
		return !getConfiguration().keys().isEmpty();
	}
	
	/**
	 * Get all blocks in this chunk which have meta
	 * 
	 * @return the meta blocks
	 */
	public GList<Block> getBlocks()
	{
		GList<String> roots = new GList<String>(getConfiguration().getRoots("blocks"));
		GList<Block> blocks = new GList<Block>();
		
		for(String i : roots)
		{
			Block block = Worlds.getWorld(gc.getWorld()).getBlockAt(Integer.valueOf(i.split("-")[0]), Integer.valueOf(i.split("-")[1]), Integer.valueOf(i.split("-")[2]));
			blocks.add(block);
		}
		
		return blocks;
	}
	
	/**
	 * Get a meta block. If it exists, and already has data, it will be
	 * returned. If it does not exist, a new linked meta instance will be
	 * returned.
	 * 
	 * @param block
	 *            the block
	 * @return the block meta or null if it does not fall within this chunk
	 */
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

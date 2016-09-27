package org.phantomapi.nest;

import org.bukkit.block.Block;
import org.phantomapi.clust.DataCluster;

/**
 * Implementation of NestedBlock
 * 
 * @author cyberpwn
 */
public class PhantomBlockNest implements NestedBlock
{
	private DataCluster cc;
	private Block block;
	
	/**
	 * Create a block nest
	 * 
	 * @param block
	 *            the block
	 * @param cc
	 *            the data cluster
	 */
	public PhantomBlockNest(Block block, DataCluster cc)
	{
		this.block = block;
		this.cc = cc;
	}
	
	public DataCluster getData()
	{
		return cc;
	}
	
	public Block getBlock()
	{
		return block;
	}
}

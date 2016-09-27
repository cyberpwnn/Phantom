package org.phantomapi.nest;

import org.bukkit.block.Block;
import org.phantomapi.clust.DataCluster;

/**
 * Represents a nested block
 * 
 * @author cyberpwn
 */
public interface NestedBlock
{
	/**
	 * Get the data cluster associated with this block. Direct modifications are
	 * fine. This is not a cloned cluster
	 * 
	 * @return the un-cloned data cluster for this block
	 */
	public DataCluster getData();
	
	/**
	 * Get the block for this nest
	 * 
	 * @return the block
	 */
	public Block getBlock();
}

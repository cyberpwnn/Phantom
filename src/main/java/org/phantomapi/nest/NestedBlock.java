package org.phantomapi.nest;

import org.bukkit.block.Block;
import org.phantomapi.clust.DataCluster;

public interface NestedBlock
{
	public DataCluster getData();

	public Block getBlock();
}

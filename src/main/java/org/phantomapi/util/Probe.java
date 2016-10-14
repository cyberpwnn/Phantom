package org.phantomapi.util;

import org.bukkit.block.Block;
import org.phantomapi.clust.DataCluster;

/**
 * Make this object probable
 * 
 * @author cyberpwn
 */
public interface Probe
{
	/**
	 * Probed objects will have this called when a player probes a block. If
	 * there is no probe information, simply return the probe set
	 * 
	 * @param block
	 *            the block that is being probed
	 * @param probeSet
	 *            the probe set to add data to
	 * @return the probeSet or modified probe set
	 */
	public DataCluster onProbe(Block block, DataCluster probeSet);
}

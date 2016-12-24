package org.phantomapi.hive;

import java.io.File;
import org.bukkit.block.Block;

/**
 * Base hyve block for a specific block
 * 
 * @author cyberpwn
 */
public class BaseHyveBlock extends BaseHyve implements HyveBlock
{
	/**
	 * Create and load in the instance of this block. Must invoke save() to save
	 * any modified changes
	 * 
	 * @param block
	 *            the block to read
	 */
	public BaseHyveBlock(Block block)
	{
		super(HyveType.BLOCK, "hyve-block." + block.getX() + "." + block.getY() + "." + block.getZ() + ".hy", new File(block.getWorld().getWorldFolder(), "block"));
		
		load();
	}
}

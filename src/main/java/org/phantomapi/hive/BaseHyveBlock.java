package org.phantomapi.hive;

import java.io.File;
import org.bukkit.block.Block;

public class BaseHyveBlock extends BaseHyve implements HyveBlock
{
	public BaseHyveBlock(Block block)
	{
		super(HyveType.BLOCK, "hyve-block." + block.getX() + "." + block.getY() + "." + block.getZ() + ".hy", new File(block.getWorld().getWorldFolder(), "block"));
		
		load();
	}
}

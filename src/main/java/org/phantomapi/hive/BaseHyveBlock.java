package org.phantomapi.hive;

import java.io.File;
import org.bukkit.block.Block;

public class BaseHyveBlock extends BaseHyve
{
	public BaseHyveBlock(Block block)
	{
		super(HyveType.BLOCK, "hyve-block." + block.getWorld().getName() + "." + block.getX() + "." + block.getY() + "." + block.getZ(), new File(block.getWorld().getWorldFolder(), "block"));
		
		load();
	}
}

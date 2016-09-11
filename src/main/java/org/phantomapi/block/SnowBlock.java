package org.phantomapi.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.SnowLevel;

/**
 * Represents a snow block for applying layers
 * 
 * @author cyberpwn
 */
public class SnowBlock implements LayeredBlock
{
	private Block block;
	
	/**
	 * Wrap a snow block
	 * 
	 * @param block
	 */
	public SnowBlock(Block block)
	{
		this.block = block;
	}

	@Override
	public void setLevel(SnowLevel level)
	{
		new MaterialBlock(Material.SNOW_BLOCK, level.getLevel()).apply(block.getLocation());
	}

	@Override
	public SnowLevel getLevel()
	{
		return new SnowLevel(new MaterialBlock(block.getLocation()).getData());
	}
	
	
}

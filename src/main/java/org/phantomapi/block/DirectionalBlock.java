package org.phantomapi.block;

import org.bukkit.block.Block;
import org.phantomapi.world.Direction;
import org.phantomapi.world.MaterialBlock;

/**
 * Represents a directional block that can change directions
 * 
 * @author cyberpwn
 */
public class DirectionalBlock implements Directional
{
	private Block block;
	
	/**
	 * Wrap a Directional block
	 * 
	 * @param block
	 */
	public DirectionalBlock(Block block)
	{
		this.block = block;
	}
	
	@Override
	public void setDirection(Direction d)
	{
		new MaterialBlock(block.getType(), d.byteValue()).apply(block.getLocation());
	}
	
	@Override
	public Direction getDirection()
	{
		return Direction.fromByte(new MaterialBlock(block.getLocation()).getData());
	}
	
}

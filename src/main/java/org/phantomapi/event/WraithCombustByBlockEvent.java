package org.phantomapi.event;

import org.bukkit.block.Block;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith exploded by the given block
 * 
 * @author cyberpwn
 */
public class WraithCombustByBlockEvent extends WraithEvent
{
	private final Block block;
	
	public WraithCombustByBlockEvent(Wraith wraith, Block block)
	{
		super(wraith);
		
		this.block = block;
	}
	
	public Block getBlock()
	{
		return block;
	}
}

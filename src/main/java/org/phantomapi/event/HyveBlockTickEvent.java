package org.phantomapi.event;

import org.bukkit.block.Block;
import org.phantomapi.hive.Hyve;

public class HyveBlockTickEvent extends HyveTickEvent
{
	private Block block;
	
	public HyveBlockTickEvent(Hyve hyve, Block block)
	{
		super(hyve);
		
		this.block = block;
	}
	
	public Block getBlock()
	{
		return block;
	}
}

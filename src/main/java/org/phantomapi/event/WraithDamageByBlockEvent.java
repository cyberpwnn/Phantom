package org.phantomapi.event;

import org.bukkit.block.Block;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith damaged by the given block
 * 
 * @author cyberpwn
 */
public class WraithDamageByBlockEvent extends WraithEvent
{
	private final Block block;
	private final Double damage;
	
	public WraithDamageByBlockEvent(Wraith wraith, Block block, Double damage)
	{
		super(wraith);
		
		this.block = block;
		this.damage = damage;
	}
	
	public Block getBlock()
	{
		return block;
	}

	public Double getDamage()
	{
		return damage;
	}
}

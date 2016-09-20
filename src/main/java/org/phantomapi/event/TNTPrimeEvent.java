package org.phantomapi.event;

import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;

public class TNTPrimeEvent extends CancellablePhantomEvent
{
	private final TNTPrimed tntEntity;
	private final Block tntBlock;
	
	public TNTPrimeEvent(TNTPrimed tntEntity, Block tntBlock)
	{
		super();
		
		this.tntEntity = tntEntity;
		this.tntBlock = tntBlock;
	}

	public TNTPrimed getTntEntity()
	{
		return tntEntity;
	}

	public Block getTntBlock()
	{
		return tntBlock;
	}
}

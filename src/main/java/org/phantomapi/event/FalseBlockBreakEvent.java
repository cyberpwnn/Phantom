package org.phantomapi.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FalseBlockBreakEvent extends CancellablePhantomEvent
{
	private Block block;
	private Player player;
	
	public FalseBlockBreakEvent(Block block, Player player)
	{
		this.block = block;
		this.player = player;
	}
	
	public Block getBlock()
	{
		return block;
	}
	
	public Player getPlayer()
	{
		return player;
	}
}

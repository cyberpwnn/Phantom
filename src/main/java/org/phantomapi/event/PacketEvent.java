package org.phantomapi.event;

import org.bukkit.entity.Player;

public class PacketEvent extends CancellablePhantomEvent
{
	private final Player player;
	private final PacketDirection direction;
	
	public PacketEvent(Player player, PacketDirection direction)
	{
		this.direction = direction;
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public PacketDirection getDirection()
	{
		return direction;
	}
}

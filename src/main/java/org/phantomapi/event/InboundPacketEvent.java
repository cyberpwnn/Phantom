package org.phantomapi.event;

import org.bukkit.entity.Player;

public class InboundPacketEvent extends PacketEvent
{
	public InboundPacketEvent(Player player)
	{
		super(player, PacketDirection.INBOUND);
	}
}

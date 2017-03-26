package org.phantomapi.event;

import org.bukkit.entity.Player;

public class OutboundPacketEvent extends PacketEvent
{
	public OutboundPacketEvent(Player player)
	{
		super(player, PacketDirection.OUTBOUND);
	}
}

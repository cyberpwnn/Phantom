package com.volmit.phantom.util.nms;

import org.bukkit.entity.Player;

public interface IPacketHandler
{
	public Object onPacketOutAsync(Player reciever, Object packet);

	public Object onPacketInAsync(Player sender, Object packet);
}

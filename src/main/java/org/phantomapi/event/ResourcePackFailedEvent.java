package org.phantomapi.event;

import org.bukkit.entity.Player;

/**
 * The player failed to get the resource pack
 * 
 * @author cyberpwn
 */
public class ResourcePackFailedEvent extends ResourcePackEvent
{
	public ResourcePackFailedEvent(Player player)
	{
		super(player);
	}
}

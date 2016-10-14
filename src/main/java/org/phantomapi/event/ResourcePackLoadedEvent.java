package org.phantomapi.event;

import org.bukkit.entity.Player;

/**
 * Resource pack loaded to the player
 * 
 * @author cyberpwn
 */
public class ResourcePackLoadedEvent extends ResourcePackEvent
{
	public ResourcePackLoadedEvent(Player player)
	{
		super(player);
	}
}

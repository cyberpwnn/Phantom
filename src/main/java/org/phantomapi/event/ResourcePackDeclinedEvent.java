package org.phantomapi.event;

import org.bukkit.entity.Player;

/**
 * The resource pack has been accepted
 * 
 * @author cyberpwn
 */
public class ResourcePackDeclinedEvent extends ResourcePackEvent
{
	public ResourcePackDeclinedEvent(Player player)
	{
		super(player);
	}
}

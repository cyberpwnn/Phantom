package org.phantomapi.event;

import org.bukkit.entity.Player;

/**
 * The Resource pack has been accepted
 * 
 * @author cyberpwn
 */
public class ResourcePackAcceptedEvent extends ResourcePackEvent
{
	public ResourcePackAcceptedEvent(Player player)
	{
		super(player);
	}
}

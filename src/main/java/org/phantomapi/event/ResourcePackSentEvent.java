package org.phantomapi.event;

import org.bukkit.entity.Player;

/**
 * A Resource pack request has been sent
 * 
 * @author cyberpwn
 */
public class ResourcePackSentEvent extends ResourcePackEvent
{
	public ResourcePackSentEvent(Player player)
	{
		super(player);
	}
}

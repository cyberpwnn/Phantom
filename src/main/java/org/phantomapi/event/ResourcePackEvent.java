package org.phantomapi.event;

import org.bukkit.entity.Player;

/**
 * Represents a resource pack event
 * 
 * @author cyberpwn
 */
public class ResourcePackEvent extends CancellablePhantomEvent
{
	private Player player;
	
	public ResourcePackEvent(Player player)
	{
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
}

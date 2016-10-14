package org.phantomapi.event;

import org.bukkit.entity.Player;

/**
 * Represents a player event
 * 
 * @author cyberpwn
 *
 */
public class PlayerEvent extends CancellablePhantomEvent
{
	private final Player player;
	
	public PlayerEvent(Player player)
	{
		this.player = player;
	}
	
	/**
	 * Get the player
	 * 
	 * @return the player
	 */
	public Player getPlayer()
	{
		return player;
	}
}

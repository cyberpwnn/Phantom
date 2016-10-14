package org.phantomapi.event;

import org.bukkit.entity.Player;

/**
 * Represents a player jump event. Called when a player jumps. However, keep in
 * mind, this works on velocities, meaning this can misfire if a player falls on
 * slime, or is thrown via plugin or explosion
 * 
 * @author cyberpwn
 *
 */
public class PlayerJumpEvent extends PlayerEvent
{
	public PlayerJumpEvent(Player player)
	{
		super(player);
	}
}

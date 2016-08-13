package org.phantomapi.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * A Player has changed their position
 * 
 * @author cyberpwn
 */
public class PlayerMovePositionEvent extends MovementEvent
{
	public PlayerMovePositionEvent(Player player, Location from, Location to)
	{
		super(player, from, to);
	}
}

package org.cyberpwn.phantom.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.event.structure.MovementEvent;

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

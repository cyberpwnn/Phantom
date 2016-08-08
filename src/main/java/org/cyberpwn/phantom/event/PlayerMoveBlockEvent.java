package org.cyberpwn.phantom.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.event.structure.MovementEvent;

/**
 * Represents a player moved at least one block from the previous position
 * 
 * @author cyberpwn
 */
public class PlayerMoveBlockEvent extends MovementEvent
{
	public PlayerMoveBlockEvent(Player player, Location from, Location to)
	{
		super(player, from, to);
	}
}

package org.phantomapi.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.phantomapi.event.structure.MovementEvent;

/**
 * A Player has changed their look position
 * 
 * @author cyberpwn
 */
public class PlayerMoveLookEvent extends MovementEvent
{
	public PlayerMoveLookEvent(Player player, Location from, Location to)
	{
		super(player, from, to);
	}
}

package org.cyberpwn.phantom.event.structure;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a movement related player event
 * 
 * @author cyberpwn
 */
public class MovementEvent extends PlayerEvent
{
	private final Location from;
	private final Location to;
	
	public MovementEvent(Player player, Location from, Location to)
	{
		super(player);
		
		this.from = from;
		this.to = to;
	}
	
	public Location getFrom()
	{
		return from;
	}
	
	public Location getTo()
	{
		return to;
	}
}

package org.phantomapi.event;

import org.bukkit.entity.Player;

public class PlayerScrollEvent extends PlayerEvent
{
	private int from;
	private int to;
	private int distance;
	private int movement;
	private ScrollDirection direction;
	
	public PlayerScrollEvent(Player player, int from, int to)
	{
		super(player);
		
		this.from = from;
		this.to = to;
		
		if(from > 7 && to == 0)
		{
			direction = ScrollDirection.DOWN;
			distance = 1;
			movement = -distance;
		}
		
		else if(from == 0 && to > 7)
		{
			direction = ScrollDirection.UP;
			distance = 1;
			movement = distance;
		}
		
		else if(from > to)
		{
			direction = ScrollDirection.UP;
			distance = from - to;
			movement = distance;
		}
		
		else if(from < to)
		{
			direction = ScrollDirection.DOWN;
			distance = to - from;
			movement = -distance;
		}
	}
	
	public int getFrom()
	{
		return from;
	}
	
	public int getTo()
	{
		return to;
	}
	
	public int getDistance()
	{
		return distance;
	}
	
	public int getMovement()
	{
		return movement;
	}
	
	public ScrollDirection getDirection()
	{
		return direction;
	}
}

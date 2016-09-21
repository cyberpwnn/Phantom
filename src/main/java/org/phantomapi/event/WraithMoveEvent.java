package org.phantomapi.event;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.phantomapi.wraith.Wraith;

/**
 * A Wraith collided with another entity
 * 
 * @author cyberpwn
 */
public class WraithMoveEvent extends WraithEvent
{
	private Vector direction;
	
	public WraithMoveEvent(Wraith wraith, Vector direction)
	{
		super(wraith);
		
		this.direction = direction;
	}
	
	/**
	 * Get velocity and direction
	 * 
	 * @return the direction
	 */
	public Vector getDirection()
	{
		return direction;
	}
	
	public Location getFrom()
	{
		return wraith.getLocation();
	}
	
	public Location getTo()
	{
		return wraith.getLocation().add(getDirection());
	}
}

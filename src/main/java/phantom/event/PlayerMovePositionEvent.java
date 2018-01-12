package phantom.event;

import org.bukkit.Location;

public class PlayerMovePositionEvent extends PhantomCancellableEvent
{
	private final Location from;
	private final Location to;
	
	public PlayerMovePositionEvent(Location from, Location to)
	{
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

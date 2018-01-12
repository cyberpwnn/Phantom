package phantom.event;

import org.bukkit.util.Vector;

public class PlayerLookEvent extends PhantomCancellableEvent
{
	private final Vector from;
	private final Vector to;
	
	public PlayerLookEvent(Vector from, Vector to)
	{
		this.from = from;
		this.to = to;
	}

	public Vector getFrom()
	{
		return from;
	}

	public Vector getTo()
	{
		return to;
	}
}

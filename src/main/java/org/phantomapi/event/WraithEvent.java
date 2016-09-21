package org.phantomapi.event;

import org.phantomapi.wraith.Wraith;

/**
 * Represents a wraith event
 * 
 * @author cyberpwn
 */
public class WraithEvent extends CancellablePhantomEvent
{
	private final Wraith wraith;
	
	public WraithEvent(Wraith wraith)
	{
		this.wraith = wraith;
	}
	
	/**
	 * Get the wraith associated with this event
	 * 
	 * @return the wraith
	 */
	public Wraith getWraith()
	{
		return wraith;
	}
}

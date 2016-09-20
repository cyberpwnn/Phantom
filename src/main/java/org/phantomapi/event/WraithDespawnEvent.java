package org.phantomapi.event;

import org.phantomapi.wraith.Wraith;

/**
 * A Wraith despawned
 * 
 * @author cyberpwn
 */
public class WraithDespawnEvent extends WraithEvent
{
	public WraithDespawnEvent(Wraith wraith)
	{
		super(wraith);
	}
}

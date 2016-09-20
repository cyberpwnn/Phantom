package org.phantomapi.event;

import org.phantomapi.wraith.Wraith;

/**
 * A Wraith spawned
 * 
 * @author cyberpwn
 */
public class WraithSpawnEvent extends WraithEvent
{
	public WraithSpawnEvent(Wraith wraith)
	{
		super(wraith);
	}
}

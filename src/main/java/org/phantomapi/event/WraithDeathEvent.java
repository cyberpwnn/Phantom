package org.phantomapi.event;

import org.phantomapi.wraith.Wraith;

/**
 * A Wraith killed
 * 
 * @author cyberpwn
 */
public class WraithDeathEvent extends WraithEvent
{
	public WraithDeathEvent(Wraith wraith)
	{
		super(wraith);
	}
}

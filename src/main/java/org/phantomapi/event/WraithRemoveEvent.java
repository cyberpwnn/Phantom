package org.phantomapi.event;

import org.phantomapi.wraith.Wraith;

/**
 * A Wraith removed
 * 
 * @author cyberpwn
 */
public class WraithRemoveEvent extends WraithEvent
{
	public WraithRemoveEvent(Wraith wraith)
	{
		super(wraith);
	}
}

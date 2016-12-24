package org.phantomapi.event;

import org.phantomapi.hive.Hyve;

public class HyveEvent extends PhantomEvent
{
	private Hyve hyve;
	
	public HyveEvent(Hyve hyve)
	{
		this.hyve = hyve;
	}
	
	public Hyve getHyve()
	{
		return hyve;
	}
}

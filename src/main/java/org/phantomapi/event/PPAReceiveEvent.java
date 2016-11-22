package org.phantomapi.event;

import org.phantomapi.ppa.PPA;

public class PPAReceiveEvent extends PhantomEvent
{
	private PPA ppa;
	
	public PPAReceiveEvent(PPA ppa)
	{
		this.ppa = ppa;
	}
	
	public PPA getPpa()
	{
		return ppa;
	}
}

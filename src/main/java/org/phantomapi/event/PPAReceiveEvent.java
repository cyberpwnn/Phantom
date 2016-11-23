package org.phantomapi.event;

import org.phantomapi.ppa.PPA;

/**
 * Called when a PPA packet is received
 * 
 * @author cyberpwn
 */
public class PPAReceiveEvent extends PhantomEvent
{
	private PPA ppa;
	
	/**
	 * Create a ppa receive event
	 * 
	 * @param ppa
	 *            the ppa paclet
	 */
	public PPAReceiveEvent(PPA ppa)
	{
		this.ppa = ppa;
	}
	
	/**
	 * Get the packet
	 * 
	 * @return the ppa packet
	 */
	public PPA getPpa()
	{
		return ppa;
	}
}

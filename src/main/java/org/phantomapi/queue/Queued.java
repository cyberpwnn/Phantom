package org.phantomapi.queue;

/**
 * Represents a queued object
 * 
 * @author cyberpwn
 */
public interface Queued
{
	/**
	 * Called when the queued object should be processed
	 */
	public void onProcess();
}

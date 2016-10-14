package org.phantomapi.async;

import org.phantomapi.Phantom;

/**
 * Simple async utils
 * 
 * @author cyberpwn
 */
public class AsyncUtil
{
	/**
	 * Enforce sync. If the invoking thread is not sync, throw an illigal state
	 * exception
	 */
	public static void enforceSync()
	{
		if(!Phantom.isSync())
		{
			throw new IllegalStateException("Cannot be invoked ASYNC");
		}
	}
	
	/**
	 * Enforce async. If the invoking thread is not async, throw an illigal
	 * state exception
	 */
	public static void enforceAsync()
	{
		if(Phantom.isSync())
		{
			throw new IllegalStateException("Cannot be invoked SYNC");
		}
	}
}

package org.phantomapi;

/**
 * Pulse signal used for pulsing signals between the api and plugin
 *
 * @author cyberpwn
 */
public enum Signal
{
	/**
	 * Startup
	 */
	START,

	/**
	 * Shut down
	 */
	STOP,

	/**
	 * Abort
	 */
	ABORT;
}

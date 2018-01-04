package org.phantomapi;

import phantom.util.metrics.Documented;

/**
 * Pulse signal used for pulsing signals between the api and plugin
 *
 * @author cyberpwn
 */
@Documented
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

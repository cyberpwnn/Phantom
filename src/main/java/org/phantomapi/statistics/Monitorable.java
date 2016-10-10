package org.phantomapi.statistics;

import org.phantomapi.registry.Registrant;

/**
 * Represents a monitorable object
 * 
 * @author cyberpwn
 */
public interface Monitorable extends Registrant
{
	/**
	 * Gets the monitored data for this object
	 * 
	 * @return the monitored data
	 */
	public String getMonitorableData();
}

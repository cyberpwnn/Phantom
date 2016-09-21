package org.phantomapi.wraith;

import org.phantomapi.lang.GList;

/**
 * Represents a handled wraith
 * 
 * @author cyberpwn
 */
public interface WraithHandled
{
	/**
	 * Get all handles for this wraith
	 * 
	 * @return the handlers
	 */
	public GList<WraithHandler> getHandlers();
	
	/**
	 * Register a handler for this wraith (auto)
	 * 
	 * @param handler
	 *            the handler
	 */
	public void registerHandler(WraithHandler handler);
	
	/**
	 * Unregister a handler for this wraith (auto)
	 * 
	 * @param handler
	 *            the handler
	 */
	public void unregisterHandler(WraithHandler handler);
	
	/**
	 * Distroy this handler (auto)
	 */
	public void destroy();
}

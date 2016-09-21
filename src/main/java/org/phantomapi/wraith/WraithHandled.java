package org.phantomapi.wraith;

import org.phantomapi.lang.GList;

public interface WraithHandled
{
	public GList<WraithHandler> getHandlers();
	
	public void registerHandler(WraithHandler handler);
	
	public void unregisterHandler(WraithHandler handler);
	
	public void destroy();
}

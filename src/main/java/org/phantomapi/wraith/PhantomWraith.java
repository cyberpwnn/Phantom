package org.phantomapi.wraith;

import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;

public class PhantomWraith extends PhantomNPCWrapper implements Wraith, WraithHandled
{
	private GList<WraithHandler> handlers;
	
	public PhantomWraith(String name)
	{
		super(name);
		
		handlers = new GList<WraithHandler>();
		Phantom.instance().getWraithController().registerWraith(this);
	}
	
	public GList<WraithHandler> getHandlers()
	{
		return handlers;
	}
	
	public void registerHandler(WraithHandler handler)
	{
		handlers.add(handler);
	}
	
	public void unregisterHandler(WraithHandler handler)
	{
		handlers.remove(handler);
		handler.unregister();
	}
	
	@Override
	public void destroy()
	{
		for(WraithHandler i : handlers.copy())
		{
			i.unregister();
		}
		
		handlers.clear();
		Phantom.instance().getWraithController().unRegisterWraith(this);
	}
}

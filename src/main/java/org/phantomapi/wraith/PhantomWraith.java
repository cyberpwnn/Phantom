package org.phantomapi.wraith;

import org.bukkit.Location;
import org.phantomapi.lang.GList;

public class PhantomWraith extends PhantomNPCWrapper implements Wraith, WraithHandled
{
	private GList<WraithHandler> handlers;
	
	public PhantomWraith(String name, String skin)
	{
		super(name, skin);
		
		handlers = new GList<WraithHandler>();
	}
	
	public PhantomWraith(String name)
	{
		this(name, name);
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
	public void despawn()
	{
		for(WraithHandler i : handlers.copy())
		{
			i.unregister();
		}
		
		handlers.clear();
	}
	}
}

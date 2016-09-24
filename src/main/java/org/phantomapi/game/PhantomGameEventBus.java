package org.phantomapi.game;

import java.lang.reflect.Method;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;

public class PhantomGameEventBus implements GameEventBus
{
	private GMap<Class<?>, GList<Method>> mapping;
	private GameController controller;
	
	public PhantomGameEventBus(GameController controller)
	{
		this.controller = controller;
		this.mapping = new GMap<Class<?>, GList<Method>>();
	}
	
	public void register(GameEventListener listener)
	{
		Class<?> c = listener.getClass();
		
		for(Method i : c.getDeclaredMethods())
		{
			if(i.isAnnotationPresent(GameEvent.class) && i.getParameterTypes().length == 1 && i.getParameterTypes()[0].isAssignableFrom(BaseGameEvent.class))
			{
				Class<?> e = i.getParameterTypes()[0];
				
				if(!mapping.containsKey(e))
				{
					mapping.put(e, new GList<Method>());
				}
				
				if(!mapping.get(e).contains(i))
				{
					mapping.get(e).add(i);
				}
			}
		}
	}
	
	public void unregister(GameEventListener listener)
	{
		Class<?> c = listener.getClass();
		
		for(Method i : c.getDeclaredMethods())
		{
			if(i.isAnnotationPresent(GameEvent.class) && i.getParameterTypes().length == 1 && i.getParameterTypes()[0].isAssignableFrom(BaseGameEvent.class))
			{
				Class<?> e = i.getParameterTypes()[0];
				
				if(!mapping.containsKey(e))
				{
					continue;
				}
				
				mapping.get(e).remove(i);
			}
		}
	}
	
	public void callEvent(BaseGameEvent event)
	{
		if(mapping.containsKey(event.getClass()))
		{
			for(Method i : mapping.get(event.getClass()))
			{
				try
				{
					if(event.isCancelled())
					{
						return;
					}
					
					i.invoke(this, event);
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public GameController getController()
	{
		return controller;
	}
}

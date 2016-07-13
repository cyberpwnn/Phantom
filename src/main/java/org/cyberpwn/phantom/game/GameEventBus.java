package org.cyberpwn.phantom.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;

public class GameEventBus<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> extends Controller
{
	private G game;
	private GMap<Class<?>, GList<Method>> eventMap;
	
	@SuppressWarnings("unchecked")
	public GameEventBus(Controllable parentController)
	{
		super(parentController);
		
		this.game = (G) parentController;
		this.eventMap = new GMap<Class<?>, GList<Method>>();
	}
	
	public G getGame()
	{
		return game;
	}
	
	public void callEvent(GameEventHolder<?, ?, ?, ?> e)
	{
		if(eventMap.containsKey(e))
		{
			for(Method i : eventMap.get(e))
			{
				try
				{
					i.invoke(this, e);
				} 
				
				catch(IllegalAccessException e1)
				{
					e1.printStackTrace();
				} 
				
				catch(IllegalArgumentException e1)
				{
					e1.printStackTrace();
				} 
				
				catch(InvocationTargetException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void registerGameListener(Class<?> clazz)
	{
		for(Method i : clazz.getMethods())
		{
			if(i.isAnnotationPresent(GameEvent.class))
			{
				if(i.getParameterCount() == 1)
				{
					Class<?> evt = i.getParameterTypes()[0];
					
					if(!eventMap.containsKey(evt))
					{
						eventMap.put(evt, new GList<Method>());
					}
					
					eventMap.get(evt).add(i);
				}
			}
		}
	}
}

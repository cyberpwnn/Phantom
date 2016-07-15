package org.cyberpwn.phantom.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;

/**
 * A Game Event Bus
 * 
 * @author cyberpwn
 *
 * @param <M>
 *            The MAP TYPE
 * @param <G>
 *            The GAME TYPE
 * @param <T>
 *            The TEAM TYPE
 * @param <P>
 *            The PLAYER OBJECT TYPE
 */
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
	
	/**
	 * The game
	 * 
	 * @return the game
	 */
	public G getGame()
	{
		return game;
	}
	
	/**
	 * Call a game event specific to this game
	 * 
	 * @param e
	 *            the event
	 */
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
	
	/**
	 * Register a game event listener to listen for THIS game specifically
	 * 
	 * @param l
	 *            the game listener
	 */
	public void registerGameListener(GameListener l)
	{
		Class<?> clazz = l.getClass();
		
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
	
	/**
	 * Unregister a game listener
	 * 
	 * @param l
	 *            the listener
	 */
	public void unregisterGameListener(GameListener l)
	{
		for(Class<?> i : eventMap.k())
		{
			for(Method j : eventMap.get(i))
			{
				if(j.getDeclaringClass().equals(l.getClass()))
				{
					eventMap.get(i).remove(j);
				}
			}
		}
	}
}

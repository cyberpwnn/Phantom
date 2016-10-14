package org.phantomapi.game;

import org.phantomapi.lang.GSet;

/**
 * Controls game handlers and manages the game event bus
 * 
 * @author cyberpwn
 */
public class PhantomGameController implements GameController
{
	protected final Game game;
	protected final GSet<GameHandler> handlers;
	protected final GameEventBus bus;
	
	/**
	 * Create a game controller
	 * 
	 * @param game
	 *            the given game
	 */
	public PhantomGameController(Game game)
	{
		this.game = game;
		this.handlers = new GSet<GameHandler>();
		this.bus = new PhantomGameEventBus(this);
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public void register(GameHandler handler)
	{
		handlers.add(handler);
	}
	
	public void unregister(GameHandler handler)
	{
		handlers.remove(handler);
	}
	
	public void unregisterAll()
	{
		handlers.clear();
	}

	public void callEvent(BaseGameEvent event)
	{
		bus.callEvent(event);
	}
	
	public GameEventBus getEventBus()
	{
		return bus;
	}
}

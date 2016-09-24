package org.phantomapi.game;

/**
 * Represents a controller of a game instance which consists of handlers
 * 
 * @author cyberpwn
 */
public interface GameController
{
	/**
	 * Get the game instance (parent)
	 * 
	 * @return get the game that holds this controller
	 */
	public Game getGame();
	
	/**
	 * Call an event to this games listeners
	 * 
	 * @param event
	 *            the event to call
	 */
	public void callEvent(BaseGameEvent event);
	
	/**
	 * Register a game handle
	 * 
	 * @param handler
	 *            the handler
	 */
	public void register(GameHandler handler);
	
	/**
	 * Unregister a game handle
	 * 
	 * @param handler
	 *            the handle
	 */
	public void unregister(GameHandler handler);
	
	/**
	 * Unregister all of the game handlers
	 */
	public void unregisterAll();
	
	/**
	 * Get the event bus
	 * 
	 * @return the game event bus
	 */
	public GameEventBus getEventBus();
}

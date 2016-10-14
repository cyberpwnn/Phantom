package org.phantomapi.game;

/**
 * Represents a game event bus for game events per game. Calling an event in one
 * game will only fire event listeners in the same game. Use the global event
 * bus for intergame events
 * 
 * @author cyberpwn
 */
public interface GameEventBus
{
	/**
	 * Register a game listener
	 * 
	 * @param listener
	 *            a game listener
	 */
	public void register(GameEventListener listener);
	
	/**
	 * Unregister a game listener
	 * 
	 * @param listener
	 *            the game listener
	 */
	public void unregister(GameEventListener listener);
	
	/**
	 * Call an event. Keep in mind that cancelled events do not call further
	 * methods.
	 * 
	 * @param event
	 *            the event
	 */
	public void callEvent(BaseGameEvent event);
	
	/**
	 * Get the game controller holding this event bus
	 * 
	 * @return the controller
	 */
	public GameController getController();
}

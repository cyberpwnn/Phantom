package org.phantomapi.game;

/**
 * Represents a game handler which allows the game to function correctly
 * 
 * @author cyberpwn
 */
public interface GameHandler
{
	/**
	 * Get the game instance
	 * 
	 * @return the game instance
	 */
	public Game getGame();
	
	/**
	 * Get the game plugin
	 * 
	 * @return the plugin
	 */
	public GamePlugin getPlugin();
	
	/**
	 * Get the game controller
	 * 
	 * @return the controller
	 */
	public GameController getController();
	
	/**
	 * Get the game state
	 * 
	 * @return the state of this game
	 */
	public GameState getState();
}

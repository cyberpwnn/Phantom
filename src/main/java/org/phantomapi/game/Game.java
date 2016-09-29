package org.phantomapi.game;

/**
 * Represents a game instance
 * 
 * @author cyberpwn
 */
public interface Game
{
	/**
	 * Start the game up after it's been initialized
	 * 
	 * @param lauchParameters
	 *            the start parameters can be used for multi game environments
	 *            or startup options. These are not needed for every type of
	 *            game, no need to use them
	 */
	public void startGame(String... lauchParameters);
	
	/**
	 * Stop the game
	 * 
	 * @param endParameters
	 *            these are used for passing data to the game when stopped, once
	 *            again, not always needed, but they are there if needed
	 */
	public void stopGame(String... endParameters);
	
	/**
	 * Get the parent controller object or plugin
	 * 
	 * @return the game plugin
	 */
	public GamePlugin getPlugin();
	
	/**
	 * Get the game controller. Used for handling the game and processing things
	 * that happen along with the event bus
	 * 
	 * @return the game controller
	 */
	public GameController getController();
	
	/**
	 * Get the game state. Used for holding objects, data and status
	 * information. Also for configurations and settings
	 * 
	 * @return the game state
	 */
	public GameState getState();
	
	/**
	 * Get the game status
	 * 
	 * @return the status of this game instance
	 */
	public GameStatus getStatus();
	
	/**
	 * Set the game status. Used for informing other handlers that the game has
	 * either started or has ended for async startups
	 * 
	 * @param status
	 *            the game instance status
	 */
	public void setStatus(GameStatus status);
	
	/**
	 * Get the game profile for defining basic things
	 * 
	 * @return the game profile
	 */
	public GameProfile getProfile();
}

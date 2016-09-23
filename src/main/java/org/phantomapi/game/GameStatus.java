package org.phantomapi.game;

/**
 * Represents a game status
 * 
 * @author cyberpwn
 */
public enum GameStatus
{
	/**
	 * The game instance exists, but the game is not running and is not starting
	 * nor stoping. Could happen before it is started or after it has stopped
	 */
	OFFLINE,
	
	/**
	 * The game is currently starting and strapping all handlers in. This is NOT
	 * when the game is waiting on players or something like that. The game is
	 * considered running even if the game is not activley being played
	 */
	STARTING,
	
	/**
	 * The game is running, meaning all handlers are functioning properly and
	 * there may or may not be players active in the game
	 */
	RUNNING,
	
	/**
	 * The game is stopping handlers, saving data and returning to the OFFLINE
	 * state
	 */
	STOPPING,
}

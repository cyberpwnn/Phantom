package org.phantomapi.game;

/**
 * Represents a game state
 * 
 * @author cyberpwn
 */
public class PhantomGameState implements GameState
{
	protected final Game game;
	
	/**
	 * Create a game state
	 * 
	 * @param game
	 *            the game instance
	 */
	public PhantomGameState(Game game)
	{
		this.game = game;
	}
	
	public Game getGame()
	{
		return game;
	}
}

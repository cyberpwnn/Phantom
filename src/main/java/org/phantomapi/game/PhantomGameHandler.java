package org.phantomapi.game;

/**
 * A Game handler
 * 
 * @author cyberpwn
 */
public class PhantomGameHandler implements GameHandler
{
	protected final Game game;
	protected final GamePlugin plugin;
	protected final GameController controller;
	protected final GameState state;
	
	/**
	 * Create a game handler
	 * 
	 * @param game
	 *            the game instance
	 */
	public PhantomGameHandler(Game game)
	{
		this.game = game;
		this.plugin = game.getPlugin();
		this.controller = game.getController();
		this.state = game.getState();
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public GamePlugin getPlugin()
	{
		return plugin;
	}
	
	public GameController getController()
	{
		return controller;
	}
	
	public GameState getState()
	{
		return state;
	}
}

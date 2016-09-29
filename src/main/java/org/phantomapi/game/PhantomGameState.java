package org.phantomapi.game;

import org.phantomapi.lang.GMap;

/**
 * Represents a game state
 * 
 * @author cyberpwn
 */
public class PhantomGameState implements GameState
{
	protected final Game game;
	private final GMap<String, GameObject> objects;
	
	/**
	 * Create a game state
	 * 
	 * @param game
	 *            the game instance
	 */
	public PhantomGameState(Game game)
	{
		this.game = game;
		this.objects = new GMap<String, GameObject>();
	}
	
	public Game getGame()
	{
		return game;
	}

	public GMap<String, GameObject> getObjects()
	{
		return objects;
	}
	
	public void registerGameObject(String key, GameObject object)
	{
		objects.put(key, object);
	}
}

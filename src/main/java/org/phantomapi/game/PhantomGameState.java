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
	private final GMap<String, GMap<String, GameObject>> objects;
	private final GMap<String, GMap<Object, String>> reference;
	
	/**
	 * Create a game state
	 * 
	 * @param game
	 *            the game instance
	 */
	public PhantomGameState(Game game)
	{
		this.game = game;
		this.objects = new GMap<String, GMap<String, GameObject>>();
		this.reference = new GMap<String, GMap<Object, String>>();
	}
	
	public Game getGame()
	{
		return game;
	}

	public GMap<String, GMap<String, GameObject>> getObjects()
	{
		return objects;
	}
	
	public GMap<String, GameObject> getObjects(String type)
	{
		return objects.get(type);
	}
	
	public GameObject get(String type, Object reference)
	{
		return getObjects(type).get(this.reference.get(type).get(reference));
	}
	
	public void reference(String type, GameObject object, String reference)
	{
		if(!this.reference.containsKey(type))
		{
			this.reference.put(type, new GMap<Object, String>());
		}
		
		this.reference.get(type).put(object, reference);
	}
}

package org.phantomapi.game;

import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;

/**
 * Represents a game state
 * 
 * @author cyberpwn
 */
public class PhantomGameState implements GameState
{
	protected final Game game;
	private final GMap<String, GameObject> objects;
	private final GSet<Player> players;
	
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
		this.players = new GSet<Player>();
	}
	
	public Game getGame()
	{
		return game;
	}

	public GMap<String, GameObject> getObjects()
	{
		return objects;
	}
	
	public void registerGameObject(GameObject object)
	{
		objects.put(object.getId(), object);
	}
	
	public void unregisterGameObject(GameObject object)
	{
		objects.remove(object.getId());
	}

	public GList<Player> getPlayers()
	{
		return new GList<Player>(players);
	}
	
	public void addPlayer(Player p)
	{
		players.add(p);
	}
	
	public void removePlayer(Player p)
	{
		players.remove(p);
	}

	@Override
	public boolean contains(Player p)
	{
		return players.contains(p);
	}

	@Override
	public GList<GameObject> getGameObjects()
	{
		return objects.v();
	}

	@Override
	public boolean contains(GameObject o)
	{
		return objects.containsKey(o.getId());
	}
}

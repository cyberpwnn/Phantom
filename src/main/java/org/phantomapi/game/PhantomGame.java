package org.phantomapi.game;

import org.bukkit.entity.Player;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GList;

/**
 * Game implementation
 * 
 * @author cyberpwn
 *
 */
public class PhantomGame extends Controller implements Game
{
	protected final Map map;
	protected final GList<GamePlayer> players;
	
	/**
	 * Create a new game instance
	 * 
	 * @param parentController
	 *            the parent controller, as this is a controller
	 * @param map
	 *            the map
	 */
	public PhantomGame(Controllable parentController, Map map)
	{
		super(parentController);
		
		this.map = map;
		this.players = new GList<GamePlayer>();
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public GList<GamePlayer> getPlayers()
	{
		return players;
	}
	
	public boolean contains(GamePlayer p)
	{
		return players.contains(p);
	}
	
	public boolean contains(Player p)
	{
		return players.contains(p);
	}
	
	public GamePlayer getPlayer(Player p)
	{
		if(contains(p))
		{
			for(GamePlayer i : players)
			{
				if(i.equals(p))
				{
					return i;
				}
			}
		}
		
		return null;
	}

	@Override
	public void onStart()
	{
		
	}

	@Override
	public void onStop()
	{
		
	}
}

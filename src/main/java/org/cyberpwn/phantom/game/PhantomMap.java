package org.cyberpwn.phantom.game;

import java.io.File;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;

/**
 * Phantom Map implementation
 * @author cyberpwn
 *
 */
public class PhantomMap implements Map
{
	private Game game;
	private World world;
	
	/**
	 * Create a phantom map
	 */
	public PhantomMap(World world)
	{
		this.game = null;
		this.world = world;
	}

	public void load(File file)
	{
		
	}

	public Game getGame()
	{
		return game;
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}

	public GList<GamePlayer> getPlayers()
	{
		return game.getPlayers();
	}

	public boolean contains(GamePlayer p)
	{
		return game.contains(p);
	}

	public boolean contains(Player p)
	{
		return game.contains(p);
	}

	public World getWorld()
	{
		return world;
	}
}

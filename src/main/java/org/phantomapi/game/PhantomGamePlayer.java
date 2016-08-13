package org.phantomapi.game;

import org.bukkit.entity.Player;

/**
 * Game Player implementation
 * 
 * @author cyberpwn
 *
 */
public class PhantomGamePlayer implements GamePlayer
{
	private final Player player;
	private final Game game;
	
	/**
	 * Create a game player from a player object
	 * 
	 * @param player
	 *            the player object
	 * @param game
	 *            the game
	 */
	public PhantomGamePlayer(Player player, Game game)
	{
		this.player = player;
		this.game = game;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public Map getMap()
	{
		return game.getMap();
	}
	
	/**
	 * Does this player equal another game player or an actual player instance
	 */
	public boolean equals(Object o)
	{
		if(o instanceof GamePlayer)
		{
			return ((GamePlayer) o).getPlayer().equals(getPlayer());
		}
		
		if(o instanceof Player)
		{
			return ((Player) o).equals(getPlayer());
		}
		
		return false;
	}
}

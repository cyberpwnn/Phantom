package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.C;

/**
 * Phantom Team implementation
 * 
 * @author cyberpwn
 *
 */
public class PhantomTeam implements Team
{
	private C color;
	private String name;
	private GList<GamePlayer> players;
	
	/**
	 * Create a phantom team
	 * 
	 * @param name
	 *            the name
	 * @param color
	 *            the color
	 */
	public PhantomTeam(String name, C color)
	{
		this.color = color;
		this.name = name;
	}
	
	public GList<GamePlayer> getPlayers()
	{
		return players;
	}
	
	public C getColor()
	{
		return color;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void add(GamePlayer player)
	{
		players.add(player);
	}
	
	public void remove(GamePlayer player)
	{
		players.remove(player);
	}
	
	public boolean contains(GamePlayer p)
	{
		return players.contains(p);
	}
	
	public boolean contains(Player p)
	{
		return players.contains(p);
	}
}

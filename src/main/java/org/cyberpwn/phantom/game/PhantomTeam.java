package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.C;

public class PhantomTeam<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> implements Team<M, G, T, P>
{
	private String name;
	private C color;
	private G game;
	private GList<Player> players;
	private GList<P> gamePlayers;
	
	public PhantomTeam(String name, C color, G game)
	{
		this.name = name;
		this.color = color;
		this.game = game;
		this.players = new GList<Player>();
		this.gamePlayers = new GList<P>();
	}
	
	@Override
	public C getColor()
	{
		return color;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public GList<Player> getPlayers()
	{
		return players;
	}
	
	@Override
	public GList<P> getGamePlayers()
	{
		return gamePlayers;
	}
	
	@Override
	public G getGame()
	{
		return game;
	}
	
	@Override
	public Integer size()
	{
		return gamePlayers.size();
	}
	
	@Override
	public Boolean contains(P p)
	{
		return gamePlayers.contains(p);
	}
	
	@Override
	public Boolean contains(Player p)
	{
		return players.contains(p);
	}
	
	@Override
	public void add(P p)
	{
		if(!contains(p))
		{
			gamePlayers.add(p);
			players.add(p.getPlayer());
		}
	}
	
	@Override
	public void remove(P p)
	{
		if(contains(p))
		{
			gamePlayers.remove(p);
			players.remove(p.getPlayer());
		}
	}
	
	@Override
	public void setColor(C color)
	{
		this.color = color;
	}
}

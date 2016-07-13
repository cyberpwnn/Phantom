package org.cyberpwn.phantom.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PhantomGamePlayer<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> implements GamePlayer<M, G, T, P>
{
	private Player player;
	private G game;
	private T team;
	
	public PhantomGamePlayer(Player player, G game)
	{
		this.player = player;
		this.game = game;
		this.team = null;
	}
	
	@Override
	public G getGame()
	{
		return game;
	}
	
	@Override
	public T getTeam()
	{
		return team;
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public void setTeam(T team)
	{
		this.team = team;
	}

	@Override
	public boolean isInMap()
	{
		return getGame().getMap().contains(getLocation());
	}

	@Override
	public Location getLocation()
	{
		return getPlayer().getLocation();
	}
	
}

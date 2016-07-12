package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;

public class PhantomGamePlayer<G extends Game<G, T, P>, T extends Team<G, T, P>, P extends GamePlayer<G, T, P>> implements GamePlayer<G, T, P>
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
	
}

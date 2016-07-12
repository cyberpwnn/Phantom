package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;

public interface GamePlayer<G, T, P>
{
	public G getGame();
	public T getTeam();
	public void setTeam(T t);
	public Player getPlayer();
}

package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;

public interface GamePlayer<G, T>
{
	public G getGame();
	public T getTeam();
	public Player getPlayer();
}

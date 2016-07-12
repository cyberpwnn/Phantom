package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;

public interface Team<G, T, P> extends Colored
{
	public String getName();
	public GList<Player> getPlayers();
	public GList<P> getGamePlayers();
	public G getGame();
	public Integer size();
	public Boolean contains(P p);
	public Boolean contains(Player p);
	public void add(P p);
	public void remove(P p);
}

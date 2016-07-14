package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;

public interface Region<R extends Region<R, M, G, T, P>, M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>>
{
	public M getMap();
	
	public G getGame();
	
	public boolean contains(P player);
	
	public boolean contains(Player player);
	
	public GList<P> getGamePlayers();
	
	public GList<Player> getPlayers();
}

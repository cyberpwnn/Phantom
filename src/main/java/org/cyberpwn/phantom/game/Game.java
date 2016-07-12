package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.C;

public interface Game<G, T extends Team<G, P>, P extends GamePlayer<G, T>>
{
	public GList<T> getTeams();
	public GList<Player> getPlayers();
	public GList<P> getGamePlayers();
	public GList<Player> getPlayers(T t);
	public GList<P> getGamePlayers(T t);
	public Boolean contains(Player p);
	public Boolean contains(P p);
	public Boolean hasTeam(T t);
	public Boolean canJoin();
	public T getTeam(String name);
	public T getTeam(C color);
	public T getTeam(P p);
	public T onSelectTeam();
	public void addTeam(T t) throws GameInitializationExeption;
	public void join(P p, T t);
	public void join(P p);
	public void quit(P p);
}

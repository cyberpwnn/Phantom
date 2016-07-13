package org.cyberpwn.phantom.game;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.C;

public class PhantomGame<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> extends Controller implements Game<M, G, T, P>
{
	private GList<T> teams;
	private GList<P> gamePlayers;
	private GList<Player> players;
	private M map;
	private UUID id;
	private GameEventBus<M, G, T, P> bus;
	
	public PhantomGame(Controllable parentController, M map)
	{
		super(parentController);
		
		this.teams = new GList<T>();
		this.players = new GList<Player>();
		this.gamePlayers = new GList<P>();
		this.map = map;
		this.id = UUID.randomUUID();
		this.bus = new GameEventBus<M, G, T, P>(this);
	}
	
	@Override
	public GList<T> getTeams()
	{
		return teams;
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
	public GList<Player> getPlayers(T t)
	{
		return t.getPlayers();
	}
	
	@Override
	public GList<P> getGamePlayers(T t)
	{
		return t.getGamePlayers();
	}
	
	@Override
	public Boolean contains(Player p)
	{
		return players.contains(p);
	}
	
	@Override
	public Boolean contains(P p)
	{
		return gamePlayers.contains(p);
	}
	
	@Override
	public Boolean hasTeam(T t)
	{
		return teams.contains(t);
	}
	
	@Override
	public Boolean canJoin()
	{
		return true;
	}
	
	@Override
	public T getTeam(String name)
	{
		for(T i : teams)
		{
			if(i.getName().equals(name))
			{
				return i;
			}
		}
		
		return null;
	}
	
	@Override
	public T getTeam(C color)
	{
		for(T i : teams)
		{
			if(i.getColor().equals(color))
			{
				return i;
			}
		}
		
		return null;
	}
	
	@Override
	public void addTeam(T t) throws GameInitializationExeption
	{
		if(getTeam(t.getColor()) != null || getTeam(t.getName()) != null)
		{
			throw new GameInitializationExeption("Team color or name already exists.");
		}
		
		teams.add(t);
	}
	
	@Override
	public void join(P p, T t)
	{
		if(canJoin() && hasTeam(t) && !contains(p))
		{
			players.add(p.getPlayer());
			gamePlayers.add(p);
			getTeam(t.getName()).add(p);
		}
	}
	
	@Override
	public void join(P p)
	{
		if(canJoin() && !contains(p))
		{
			players.add(p.getPlayer());
			gamePlayers.add(p);
			onSelectTeam().add(p);
		}
	}
	
	@Override
	public void quit(P p)
	{
		if(contains(p))
		{
			getTeam(p).remove(p);
			players.remove(p.getPlayer());
			gamePlayers.remove(p);
		}
	}
	
	@Override
	public T onSelectTeam()
	{
		return teams.pickRandom();
	}
	
	@Override
	public T getTeam(P p)
	{
		for(T i : teams)
		{
			if(i.contains(p))
			{
				return i;
			}
		}
		
		return null;
	}

	@Override
	public M getMap()
	{
		return map;
	}

	@Override
	public UUID getId()
	{
		return id;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o != null)
		{
			if(o instanceof Game)
			{
				Game<?, ?, ?, ?> g = (Game<?, ?, ?, ?>) o;
				
				if(getId().equals(g.getId()))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public GameEventBus<M, G, T, P> getBus()
	{
		return bus;
	}
}

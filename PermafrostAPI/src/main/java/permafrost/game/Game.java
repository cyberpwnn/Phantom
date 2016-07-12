package permafrost.game;

import java.util.UUID;

import org.bukkit.entity.Player;

import permafrost.core.lang.GList;

public class Game implements Playable
{
	protected final GList<GamePlayer> players;
	protected final UUID uuid;
	
	public Game()
	{
		players = new GList<GamePlayer>();
		uuid = UUID.randomUUID();
	}
	
	@Override
	public void start()
	{
		
	}

	@Override
	public void stop()
	{
		
	}

	@Override
	public void join(Player player)
	{
		if(contains(player))
		{
			return;
		}
	}

	@Override
	public void quit(Player player)
	{
		if(!contains(player))
		{
			return;
		}
	}

	@Override
	public GList<Player> getPlayers()
	{
		GList<Player> players = new GList<Player>();
		
		for(GamePlayer i : this.players)
		{
			players.add(i.getPlayer());
		}
		
		return players;
	}

	@Override
	public GList<GamePlayer> getGamePlayers()
	{
		return players.copy();
	}

	@Override
	public GamePlayer getGamePlayer(Player player)
	{
		for(GamePlayer i : getGamePlayers())
		{
			if(i.getPlayer().equals(player))
			{
				return i;
			}
		}
		
		return null;
	}

	@Override
	public boolean contains(Player player)
	{
		return getPlayers().contains(player);
	}
	
	@Override
	public UUID getId()
	{
		return uuid;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o != null)
		{
			if(o instanceof Playable)
			{
				Playable p = (Playable) o;
				
				return p.getId().equals(getId());
			}
		}
		
		return false;
	}
}

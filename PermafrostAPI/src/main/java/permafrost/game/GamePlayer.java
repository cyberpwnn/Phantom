package permafrost.game;

import org.bukkit.entity.Player;

public class GamePlayer implements GamePlayable
{
	protected final Player player;
	
	public GamePlayer(Player player)
	{
		this.player = player;
	}

	public Player getPlayer()
	{
		return player;
	}
	
	public boolean equals(Object o)
	{
		if(o != null)
		{
			if(o instanceof GamePlayer)
			{
				GamePlayer p = (GamePlayer) o;
				
				return p.getPlayer().equals(getPlayer());
			}
		}
		
		return false;
	}
}

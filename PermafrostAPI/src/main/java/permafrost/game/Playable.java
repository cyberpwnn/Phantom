package permafrost.game;

import java.util.UUID;

import org.bukkit.entity.Player;

import permafrost.core.lang.GList;

public interface Playable
{
	void start();
	void stop();
	void join(Player player);
	void quit(Player player);
	boolean contains(Player player);
	UUID getId();
	GList<Player> getPlayers();
	GList<GamePlayer> getGamePlayers();
	GamePlayer getGamePlayer(Player player);
}

package phantom.entity;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import phantom.lang.GList;
import phantom.lang.GListAdapter;
import phantom.util.metrics.Documented;

/**
 * Player utilities
 *
 * @author cyberpwn
 */
@Documented
public class Players
{
	/**
	 * Checks if anyone is online
	 *
	 * @return returns true if there is at least one player online.
	 */
	public static boolean isAnyoneOnline()
	{
		return !getPlayers().isEmpty();
	}

	/**
	 * Get the player count
	 *
	 * @return the player count
	 */
	public static int getPlayerCount()
	{
		return getPlayers().size();
	}

	/**
	 * Gets the first player in the player list. Will remain the same every time
	 * until the list is modified.
	 *
	 * @return any player (semi-consistent)
	 */
	public static Player getAnyPlayer()
	{
		if(isAnyoneOnline())
		{
			return getPlayers().get(0);
		}

		return null;
	}

	/**
	 * Randomly pick a player
	 *
	 * @return a random player or null if no one is online.
	 */
	public static Player getRandomPlayer()
	{
		if(isAnyoneOnline())
		{
			return getPlayers().pickRandom();
		}

		return null;
	}

	/**
	 * Get the player based on uuid
	 *
	 * @param id
	 *            the player uid
	 * @return the player matching the uuid or null
	 */
	public static Player getPlayer(UUID id)
	{
		return Bukkit.getPlayer(id);
	}

	/**
	 * Get the player matching a name. First it looks for identical matches, then
	 * tries ignoring case, then finally checks for partial ignored case.
	 *
	 * @param search
	 *            the search query
	 * @return the player found or null
	 */
	public static Player getPlayer(String search)
	{
		GList<Player> players = getPlayers();
		for(Player i : players)
		{
			if(i.getName().equals(search))
			{
				return i;
			}
		}

		for(Player i : players)
		{
			if(i.getName().equalsIgnoreCase(search))
			{
				return i;
			}
		}

		for(Player i : players)
		{
			if(i.getName().toLowerCase().contains(search.toLowerCase()))
			{
				return i;
			}
		}

		return null;
	}

	/**
	 * Search for multiple player matches. If there is an identical match, nothing
	 * else will be searched. If there is multiple ignored case matches, partials
	 * will not be matched. Else it will match all partials.
	 *
	 * @param search
	 *            the search query
	 * @return a list of partial matches
	 */
	public GList<Player> getPlayers(String search)
	{
		GList<Player> players = getPlayers();
		GList<Player> found = new GList<Player>();

		for(Player i : players)
		{
			if(i.getName().equals(search))
			{
				found.add(i);
			}
		}

		if(!found.isEmpty())
		{
			return found.removeDuplicates();
		}

		for(Player i : players)
		{
			if(i.getName().equalsIgnoreCase(search))
			{
				found.add(i);
			}
		}

		if(!found.isEmpty())
		{
			return found.removeDuplicates();
		}

		for(Player i : players)
		{
			if(i.getName().toLowerCase().contains(search.toLowerCase()))
			{
				found.add(i);
			}
		}

		return found.removeDuplicates();
	}

	/**
	 * Returns a list of ops
	 *
	 * @return ops in glist
	 */
	public static GList<Player> getPlayerWithOps()
	{
		return getPlayers(new GListAdapter<Player, Player>()
		{
			@Override
			public Player onAdapt(Player from)
			{
				return from.isOp() ? from : null;
			}
		});
	}

	/**
	 * Returns a list of non ops
	 *
	 * @return non op list
	 */
	public static GList<Player> getPlayerWithoutOps()
	{
		return getPlayers(new GListAdapter<Player, Player>()
		{
			@Override
			public Player onAdapt(Player from)
			{
				return from.isOp() ? null : from;
			}
		});
	}

	/**
	 * Returns a list of players who have all of the given permissions.
	 *
	 * @param permissions
	 *            a collection of permissions any player in the return list must
	 *            have. Supplying no permissions will return the source list.
	 * @return a glist of players
	 */
	public static GList<Player> getPlayersWithPermission(String... permissions)
	{
		return getPlayers(new GListAdapter<Player, Player>()
		{
			@Override
			public Player onAdapt(Player from)
			{
				for(String i : permissions)
				{
					if(!from.hasPermission(i))
					{
						return null;
					}
				}

				return from;
			}
		});
	}

	/**
	 * Returns a list of players who do not have all of the given permissions.
	 *
	 * @param permissions
	 *            a collection of permissions any player in the return list cannot
	 *            have. Supplying no permissions will return the source list.
	 * @return a glist of players
	 */
	public static GList<Player> getPlayersWithoutPermission(String... permissions)
	{
		return getPlayers(new GListAdapter<Player, Player>()
		{
			@Override
			public Player onAdapt(Player from)
			{
				for(String i : permissions)
				{
					if(from.hasPermission(i))
					{
						return null;
					}
				}

				return from;
			}
		});
	}

	/**
	 * Get a list of players who match the given adapter. Source list comes from all
	 * players currently connected.
	 *
	 * @param adapter
	 *            the glist adapter to determine if the player should be adapted to
	 *            the next list. If the adapter returns null instead of the source
	 *            player, it will not be added to the list.
	 * @return the adapted list of players
	 */
	public static GList<Player> getPlayers(GListAdapter<Player, Player> adapter)
	{
		return adapter.adapt(getPlayers());
	}

	/**
	 * Returns a glist of all players currently online
	 *
	 * @return a glist representing all online players
	 */
	public static GList<Player> getPlayers()
	{
		GList<Player> p = new GList<Player>();

		for(Player i : Bukkit.getOnlinePlayers())
		{
			p.add(i);
		}

		return p;
	}
}

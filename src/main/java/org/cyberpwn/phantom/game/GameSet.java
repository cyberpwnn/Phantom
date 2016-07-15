package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.lang.Alphabet;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.sync.TaskLater;

/**
 * A Game Set for multiple games
 * 
 * @author cyberpwn
 *
 * @param <M>
 *            The MAP TYPE
 * @param <G>
 *            The GAME TYPE
 * @param <T>
 *            The TEAM TYPE
 * @param <P>
 *            The PLAYER OBJECT TYPE
 */
public class GameSet<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>>
{
	private final GList<G> games;
	private final Integer playerLimit;
	private final Integer gameLimit;
	private final GameAdapter<M, G, T, P> gameAdapter;
	
	/**
	 * Define the game set
	 * 
	 * @param gameAdapter
	 *            the adapter
	 * @param playerLimit
	 *            the max limit of players PER GAME
	 * @param gameLimit
	 *            the max limit of games
	 */
	public GameSet(GameAdapter<M, G, T, P> gameAdapter, int playerLimit, int gameLimit)
	{
		this.gameAdapter = gameAdapter;
		this.games = new GList<G>();
		this.playerLimit = playerLimit;
		this.gameLimit = gameLimit;
	}
	
	private Alphabet nextAlphabet()
	{
		GList<Alphabet> a = new GList<Alphabet>(Alphabet.values());
		
		for(G i : games)
		{
			a.remove(i.getAlpha());
		}
		
		if(!a.isEmpty())
		{
			return a.get(0);
		}
		
		return null;
	}
	
	private void newGame(Alphabet alphabet)
	{
		G g = gameAdapter.onGameCreate(alphabet);
		((Controllable) g).start();
		games.add(g);
	}
	
	/**
	 * Quit a player and handle stopping games if needed
	 * 
	 * @param p
	 *            the player
	 */
	public void quit(Player p)
	{
		for(G i : games.copy())
		{
			if(i.contains(p))
			{
				i.quit(i.getGamePlayer(p));
				
				if(i.getPlayers().isEmpty())
				{
					((Controllable) i).stop();
					games.remove(i);
				}
				
				return;
			}
		}
	}
	
	/**
	 * Join the player to a game creating a new one if needed
	 * 
	 * @param p
	 *            the player
	 * @return true if success
	 */
	public boolean join(Player p)
	{
		for(G i : games)
		{
			if(i.getPlayers().size() < playerLimit)
			{
				P o = gameAdapter.onPlayerObjectCreate(i, p);
				i.join(o);
				
				return true;
			}
		}
		
		if(games.size() < gameLimit)
		{
			newGame(nextAlphabet());
			
			new TaskLater()
			{
				public void run()
				{
					join(p);
				}
			};
		}
		
		return false;
	}
	
	/**
	 * Get all games
	 * 
	 * @return the list of games
	 */
	public GList<G> getGames()
	{
		return games;
	}
	
	/**
	 * Get the player limit
	 * 
	 * @return the limit
	 */
	public Integer getPlayerLimit()
	{
		return playerLimit;
	}
	
	/**
	 * Get the game limit
	 * 
	 * @return the game limit
	 */
	public Integer getGameLimit()
	{
		return gameLimit;
	}
	
	/**
	 * Get the game adapter
	 * 
	 * @return the adapter
	 */
	public GameAdapter<M, G, T, P> getGameAdapter()
	{
		return gameAdapter;
	}
}

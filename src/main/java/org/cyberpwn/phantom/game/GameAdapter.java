package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.Alphabet;

/**
 * A Game adapter for handing new games and players
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
public interface GameAdapter<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>>
{
	/**
	 * Called when the game is created. This is needed to create a new instance
	 * of a game with a map.
	 * 
	 * @param alphabet
	 *            an alphabetical enum for identifying this game versus other
	 *            games of the same type
	 * @return the game object
	 */
	public G onGameCreate(Alphabet alphabet);
	
	/**
	 * Called when a player joins and needs a player object.
	 * 
	 * @param g
	 *            the game object
	 * @param p
	 *            the player
	 * @return the player object of the type P
	 * 
	 */
	public P onPlayerObjectCreate(G g, Player p);
}

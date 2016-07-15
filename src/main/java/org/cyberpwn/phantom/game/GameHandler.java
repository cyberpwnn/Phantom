package org.cyberpwn.phantom.game;

/**
 * A Game handler
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
public interface GameHandler<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> extends GameListener
{
	/**
	 * Get the game
	 * 
	 * @return the game
	 */
	public G getGame();
	
	/**
	 * Get the game map
	 * 
	 * @return the game map
	 */
	public M getMap();
}

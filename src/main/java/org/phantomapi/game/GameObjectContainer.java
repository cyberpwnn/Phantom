package org.phantomapi.game;

import org.phantomapi.lang.GList;

/**
 * Contains Game Objects
 * 
 * @author cyberpwn
 */
public interface GameObjectContainer
{
	/**
	 * Get GameObjects in this container
	 * 
	 * @return the GameObjects
	 */
	public GList<GameObject> getGameObjects();
	
	/**
	 * Does the container contain the given GameObject?
	 * 
	 * @param o
	 *            the GameObject
	 * @return true if it does
	 */
	public boolean contains(GameObject o);
}

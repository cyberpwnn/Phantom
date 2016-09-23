package org.phantomapi.game;

import org.phantomapi.clust.DataEntity;

/**
 * Represents a game object
 * 
 * @author cyberpwn
 */
public interface GameObject extends DataEntity
{
	/**
	 * Get the type of game object
	 * 
	 * @return the game type
	 */
	public String getType();
	
	/**
	 * Get the id of this object
	 * 
	 * @return the game object id
	 */
	public String getId();
}

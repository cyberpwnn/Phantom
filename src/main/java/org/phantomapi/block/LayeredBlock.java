package org.phantomapi.block;

import org.phantomapi.world.SnowLevel;

/**
 * Represents a layered block (snow)
 * 
 * @author cyberpwn
 */
public interface LayeredBlock
{
	/**
	 * Set the layer level
	 * 
	 * @param level
	 *            the level
	 */
	public void setLevel(SnowLevel level);
	
	/**
	 * Get the snowlevel object representing this layer level
	 * 
	 * @return the snow level object
	 */
	public SnowLevel getLevel();
}

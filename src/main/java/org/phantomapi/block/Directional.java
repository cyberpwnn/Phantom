package org.phantomapi.block;

import org.phantomapi.world.Direction;

/**
 * Represents a directional block
 * 
 * @author cyberpwn
 */
public interface Directional
{
	/**
	 * Set the direction of this block
	 * 
	 * @param d
	 *            the direction
	 */
	public void setDirection(Direction d);
	
	/**
	 * Get the direction of this block
	 * 
	 * @return the direction of the block
	 */
	public Direction getDirection();
}

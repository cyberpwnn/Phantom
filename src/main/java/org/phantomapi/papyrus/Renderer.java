package org.phantomapi.papyrus;

import org.phantomapi.lang.ByteMap2D;
import org.phantomapi.world.Dimension;

/**
 * Represents a renderer
 * 
 * @author cyberpwn
 */
public interface Renderer
{
	/**
	 * Get the dimensions of this renderer
	 * 
	 * @return the dimensions
	 */
	public Dimension getDimension();
	
	/**
	 * Get the byte map for this renderer
	 * 
	 * @return the byte map
	 */
	public ByteMap2D getByteMap();
	
	/**
	 * Set the given color
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param color
	 *            the color
	 */
	public void set(int x, int y, byte color);
	
	/**
	 * Get the color
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the color
	 */
	public byte get(int x, int y);
	
	/**
	 * Clear the renderer
	 * 
	 * @param x1
	 *            the first x
	 * @param y1
	 *            the first y
	 * @param x2
	 *            the second x
	 * @param y2
	 *            the second y
	 * @param color
	 *            the color
	 */
	public void clear(int x1, int y1, int x2, int y2, byte color);
	
	/**
	 * Clear the entire renderer
	 * 
	 * @param color
	 *            the color
	 */
	public void clear(byte color);
	
	/**
	 * Clear the entire renderer white
	 */
	public void clear();
	
	/**
	 * Render the render
	 */
	public void render();
}

package org.cyberpwn.phantom.world;

/**
 * Dimensions
 * 
 * @author cyberpwn
 *
 */
public class Dimension
{
	private final int width;
	private final int height;
	private final int depth;
	
	/**
	 * Make a dimension
	 * @param width width of this (X) 
	 * @param height the height (Y)
	 * @param depth the depth  (Z)
	 */
	public Dimension(int width, int height, int depth)
	{
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getDepth()
	{
		return depth;
	}
}

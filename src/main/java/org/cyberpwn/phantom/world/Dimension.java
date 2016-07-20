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

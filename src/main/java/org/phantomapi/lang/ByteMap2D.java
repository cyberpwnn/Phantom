package org.phantomapi.lang;

/**
 * Two dimensional byte map
 * 
 * @author cyberpwn
 */
public class ByteMap2D
{
	private final byte[][] map;
	private final int width;
	private final int height;
	
	/**
	 * Create a byte map
	 * 
	 * @param x
	 *            the width
	 * @param y
	 *            the height
	 * @param b
	 *            the byte to fill it initially with
	 */
	public ByteMap2D(int x, int y, byte b)
	{
		this.map = new byte[x][y];
		this.width = x;
		this.height = y;
		
		clear(b);
	}
	
	/**
	 * Clear the map
	 * 
	 * @param x1
	 *            the first x
	 * @param y1
	 *            the first y
	 * @param x2
	 *            the second x
	 * @param y2
	 *            the second y
	 * @param b
	 *            the byte to clear it with
	 */
	public void clear(int x1, int y1, int x2, int y2, byte b)
	{
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				set(x, y, b);
			}
		}
	}
	
	/**
	 * Clear the entire byte map with the given byte
	 * 
	 * @param b
	 *            the given byte
	 */
	public void clear(byte b)
	{
		clear(0, 0, width, height, b);
	}
	
	/**
	 * Set the given x and y to a byte
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param b
	 *            the byte
	 */
	public void set(int x, int y, byte b)
	{
		map[x][y] = b;
	}
	
	/**
	 * Get the given x and y byte
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the byte
	 */
	public byte get(int x, int y)
	{
		return map[x][y];
	}
	
	/**
	 * Get the byte array
	 * 
	 * @return the byte array
	 */
	public byte[][] getMap()
	{
		return map;
	}
	
	/**
	 * Get the width
	 * 
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Get the height
	 * 
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}
}

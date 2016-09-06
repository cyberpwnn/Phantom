package org.phantomapi.papyrus;

import java.awt.image.BufferedImage;

/**
 * Paper image
 * 
 * @author cyberpwn
 */
public class Paper
{
	private byte[][] image;
	
	/**
	 * Create a paper instance
	 */
	public Paper()
	{
		image = new byte[128][128];
		
		for(int y = 0; y < 128; y++)
		{
			for(int x = 0; x < 128; x++)
			{
				image[x][y] = PaperColor.WHITE;
			}
		}
	}
	
	/**
	 * Set a pixel
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param color
	 *            the color byte
	 */
	public void set(int x, int y, byte color)
	{
		image[x][y] = color;
	}
	
	/**
	 * Get the image
	 * 
	 * @return returns an x, y dimensional array of bytes
	 */
	public byte[][] getImage()
	{
		return image;
	}
	
	/**
	 * Set the image of bytes
	 * 
	 * @param image
	 *            the bytes
	 */
	public void setImage(byte[][] image)
	{
		this.image = image;
	}
	
	/**
	 * Set the image
	 * 
	 * @param image
	 *            the buffered image
	 */
	public void setImage(BufferedImage image)
	{
		for(int y = 0; y < 128; y++)
		{
			for(int x = 0; x < 128; x++)
			{
				this.image[x][y] = PaperColor.matchColor(PaperColor.getColor((byte) image.getRGB(x, y)));
			}
		}
	}
	
	/**
	 * Set the image of bytes from a listed set of bytes
	 * 
	 * @param image
	 *            the listed bytes
	 */
	public void setImage(byte[] image)
	{
		int ix = 0;
		
		for(int y = 0; y < 128; y++)
		{
			for(int x = 0; x < 128; x++)
			{
				this.image[x][y] = image[ix];
				ix++;
			}
		}
	}
	
	/**
	 * Clear the map
	 * 
	 * @param color
	 *            the color to clear with
	 */
	public void clear(byte color)
	{
		for(int y = 0; y < 128; y++)
		{
			for(int x = 0; x < 128; x++)
			{
				image[x][y] = color;
			}
		}
	}
}

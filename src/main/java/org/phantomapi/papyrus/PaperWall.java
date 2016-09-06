package org.phantomapi.papyrus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * Create a wall of papers for an image
 * 
 * @author cyberpwn
 */
public class PaperWall
{
	private final Paper[][] map;
	private final int x;
	private final int y;
	
	/**
	 * Create a paper wall with a width and height of papers
	 * 
	 * @param x
	 *            the x width
	 * @param y
	 *            the y height
	 */
	public PaperWall(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.map = new Paper[x][y];
		
		for(int i = 0; i < x; i++)
		{
			for(int j = 0; j < y; j++)
			{
				map[i][j] = new Paper();
			}
		}
	}
	
	/**
	 * Set the given pixel
	 * 
	 * @param x
	 *            the x pixel
	 * @param y
	 *            the y pixel
	 * @param color
	 *            the color
	 */
	public void set(int x, int y, byte color)
	{
		int xs = x >> 7;
		int ys = y >> 7;
		int xp = x - (128 * xs);
		int yp = y - (128 * ys);
		
		if((xs > this.x || xs < 0) || ys > this.y || ys < 0)
		{
			return;
		}
		
		map[xs][ys].set(xp, yp, color);
	}
	
	/**
	 * Draw an image
	 * 
	 * @param icon
	 *            the image
	 * @param fit
	 *            force fit it?
	 */
	public void drawImage(ImageIcon icon, boolean fit)
	{
		BufferedImage r = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = r.createGraphics();
		graphics.drawImage(icon.getImage(), 0, 0, x, y, null);
		graphics.dispose();
		
		BufferedImage temp = new BufferedImage(r.getWidth(null), r.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2 = temp.createGraphics();
		graphics2.drawImage(r, 0, 0, null);
		graphics2.dispose();
		
		int[] pixels = new int[temp.getWidth() * temp.getHeight()];
		temp.getRGB(0, 0, temp.getWidth(), temp.getHeight(), pixels, 0, temp.getWidth());
		byte[] result = new byte[temp.getWidth() * temp.getHeight()];
		int mx = 0;
		
		for(int i = 0; i < pixels.length; i++)
		{
			result[i] = PaperColor.matchColor(new Color(pixels[i], true));
		}
		
		for(int i = 0; i < (x * 128); i++)
		{
			for(int j = 0; j < (y * 128); j++)
			{
				set(i, j, result[mx]);
				mx++;
			}
		}
	}
	
	/**
	 * Clear the entire wall
	 * 
	 * @param color
	 *            the color
	 */
	public void clear(byte color)
	{
		for(int i = 0; i < x; i++)
		{
			for(int j = 0; j < y; j++)
			{
				map[i][j].clear(color);
			}
		}
	}
	
	/**
	 * Get the papers
	 * 
	 * @return the papers
	 */
	public Paper[][] getPapers()
	{
		return map;
	}
}

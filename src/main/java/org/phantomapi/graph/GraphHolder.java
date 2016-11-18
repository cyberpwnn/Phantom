package org.phantomapi.graph;

import java.awt.Color;

/**
 * Represents a graphable plane
 * 
 * @author cyberpwn
 */
public interface GraphHolder
{
	/**
	 * Get the width of this graphable plane
	 * 
	 * @return the width
	 */
	public int getGraphWidth();
	
	/**
	 * Get the height of this graphable plane
	 * 
	 * @return the height
	 */
	public int getGraphHeight();
	
	/**
	 * Draw a pixel on the plane
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param color
	 *            the color
	 */
	public void drawGraph(int x, int y, Color color);
}

package org.phantomapi.papyrus;

/**
 * Render filter
 * 
 * @author cyberpwn
 */
public abstract class RenderFilter
{
	/**
	 * Render next
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param currentColor
	 *            the current color
	 * @return the next color
	 */
	public abstract byte onRender(int x, int y, byte currentColor);
}

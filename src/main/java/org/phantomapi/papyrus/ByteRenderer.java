package org.phantomapi.papyrus;

import org.phantomapi.lang.ByteMap2D;
import org.phantomapi.world.Dimension;

/**
 * Papyrus renderer
 * 
 * @author cyberpwn
 */
public abstract class ByteRenderer implements Renderer
{
	private final Dimension dimension;
	private final ByteMap2D byteMap;
	
	/**
	 * Create a renderer
	 * 
	 * @param dimension
	 *            the dimension
	 */
	public ByteRenderer(Dimension dimension)
	{
		this.dimension = dimension;
		this.byteMap = new ByteMap2D(dimension.getWidth(), dimension.getHeight(), PaperColor.WHITE);
	}
	
	@Override
	public Dimension getDimension()
	{
		return dimension;
	}
	
	@Override
	public ByteMap2D getByteMap()
	{
		return byteMap;
	}
	
	@Override
	public void set(int x, int y, byte color)
	{
		byteMap.set(x, y, color);
	}
	
	@Override
	public byte get(int x, int y)
	{
		return byteMap.get(x, y);
	}
	
	@Override
	public void clear(int x1, int y1, int x2, int y2, byte color)
	{
		byteMap.clear(x1, y1, x2, y2, color);
	}
	
	@Override
	public void clear(byte color)
	{
		byteMap.clear(color);
	}
	
	@Override
	public void clear()
	{
		clear(PaperColor.WHITE);
	}
	
	@Override
	public abstract void render();
}

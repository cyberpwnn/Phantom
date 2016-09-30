package org.phantomapi.papyrus;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.phantomapi.lang.ByteMap2D;
import org.phantomapi.lang.GBiset;
import org.phantomapi.lang.GList;
import org.phantomapi.world.Dimension;

/**
 * Papyrus renderer
 * 
 * @author cyberpwn
 */
public abstract class ByteRenderer extends MapRenderer implements Renderer
{
	private final Dimension dimension;
	private final ByteMap2D byteMap;
	private final GList<GBiset<Integer, Integer>> changes;
	
	/**
	 * Create a renderer
	 * 
	 * @param dimension
	 *            the dimension
	 */
	public ByteRenderer(Dimension dimension)
	{
		this.dimension = dimension;
		this.changes = new GList<GBiset<Integer, Integer>>();
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
		if(get(x, y) != color)
		{
			byteMap.set(x, y, color);
			changes.add(new GBiset<Integer, Integer>(x, y));
		}
	}
	
	@Override
	public byte get(int x, int y)
	{
		return byteMap.get(x, y);
	}
	
	@Override
	public void clear(int x1, int y1, int x2, int y2, byte color)
	{
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				set(x, y, color);
			}
		}
	}
	
	@Override
	public void clear(byte color)
	{
		clear(0, 0, byteMap.getWidth(), byteMap.getHeight(), color);
	}
	
	@Override
	public void clear()
	{
		clear(PaperColor.WHITE);
	}
	
	@Override
	public abstract void render();

	@Override
	public void render(MapView view, MapCanvas canvas, Player player)
	{
		changes.clear();
		render();
		
		if(!changes.isEmpty())
		{
			for(GBiset<Integer, Integer> i : changes)
			{
				canvas.setPixel(i.getA(), i.getB(), get(i.getA(), i.getB()));
			}
		}
	}
}

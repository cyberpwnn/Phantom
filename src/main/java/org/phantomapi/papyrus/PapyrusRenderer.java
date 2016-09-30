package org.phantomapi.papyrus;

import org.bukkit.map.MapView;
import org.phantomapi.world.Dimension;

/**
 * Represents a papyrus renderer for one map rendering
 * 
 * @author cyberpwn
 */
public abstract class PapyrusRenderer extends ByteRenderer
{
	/**
	 * Papyrus renderer
	 * 
	 * @param view
	 *            the map view
	 */
	public PapyrusRenderer(MapView view)
	{
		super(new Dimension(128, 128));
		Maps.clearRenderers(view);
		view.addRenderer(this);
	}
	
	/**
	 * Filter through the frame
	 * 
	 * @param filter
	 *            the filter
	 */
	public void filter(RenderFilter filter)
	{
		for(int x = 0; x < getDimension().getWidth(); x++)
		{
			for(int y = 0; y < getDimension().getHeight(); y++)
			{
				byte current = get(x, y);
				byte next = filter.onRender(x, y, current);
				
				if(current != next)
				{
					set(x, y, next);
				}
			}
		}
	}
	
	@Override
	public abstract void render();
}

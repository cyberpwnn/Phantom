package org.phantomapi.papyrus;

import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.phantomapi.lang.GList;

/**
 * Paper utils
 * 
 * @author cyberpwn
 */
public class PaperUtils
{
	/**
	 * Add a paper renderer to the map
	 * 
	 * @param renderer
	 *            the renderer
	 * @param map
	 *            the map
	 */
	public static void addRenderer(PaperRenderer renderer, MapView map)
	{
		map.addRenderer(renderer);
	}
	
	/**
	 * Clear all map renderers
	 * 
	 * @param map
	 *            the map
	 */
	public static void clearRenderers(MapView map)
	{
		for(MapRenderer i : new GList<MapRenderer>(map.getRenderers()))
		{
			map.removeRenderer(i);
		}
		
		map.getRenderers().clear();
	}
}

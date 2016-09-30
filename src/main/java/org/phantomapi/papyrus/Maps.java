package org.phantomapi.papyrus;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.phantomapi.lang.GList;

/**
 * Map utils
 * 
 * @author cyberpwn
 */
public class Maps
{
	/**
	 * Get a map view from an item stack
	 * 
	 * @param is
	 *            the item stack
	 * @return the map view
	 */
	@SuppressWarnings("deprecation")
	public static MapView getView(ItemStack is)
	{
		return Bukkit.getMap(is.getDurability());
	}
	
	/**
	 * Clear map renderers
	 * 
	 * @param map
	 *            the map view
	 */
	public static void clearRenderers(MapView map)
	{
		map.getRenderers().clear();
		
		for(MapRenderer i : new GList<MapRenderer>(map.getRenderers()))
		{
			map.removeRenderer(i);
		}
	}
}

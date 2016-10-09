package org.phantomapi.world;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;

/**
 * The World queue
 * 
 * @author cyberpwn
 */
public class WQ extends PhantomWorldQueue
{
	/**
	 * Color a block to the given location
	 * 
	 * @param location
	 *            the location
	 * @param color
	 *            the color
	 */
	@SuppressWarnings("deprecation")
	public void color(Location location, DyeColor color)
	{
		if(location.getBlock().getType().equals(Material.BANNER) || location.getBlock().getType().equals(Material.WALL_BANNER) || location.getBlock().getType().equals(Material.STANDING_BANNER))
		{
			Banner banner = (Banner) location.getBlock().getState();
			banner.setBaseColor(color);
			banner.update();
			
			return;
		}
		
		new MaterialBlock(location.getBlock().getType(), color.getData()).apply(location.getBlock().getLocation());
	}
}

package org.phantomapi.world;

import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Biome;

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
	
	public void setBiome(Chunk chunk, Biome biome)
	{
		for(int i = 0; i < 16; i++)
		{
			for(int j = 0; j < 16; j++)
			{
				setBiome(chunk.getBlock(i, 0, j).getLocation(), biome);
			}
		}
	}
}

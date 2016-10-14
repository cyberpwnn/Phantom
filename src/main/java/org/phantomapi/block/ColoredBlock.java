package org.phantomapi.block;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.phantomapi.world.MaterialBlock;

/**
 * Represents a colored block wrapper
 * 
 * @author cyberpwn
 */
public class ColoredBlock
{
	private Block block;
	
	/**
	 * Wrap a colored block
	 * 
	 * @param block
	 *            the colorable block
	 */
	public ColoredBlock(Block block)
	{
		this.block = block;
	}
	
	/**
	 * Set the color of this block
	 * 
	 * @param dc
	 *            the block
	 */
	@SuppressWarnings("deprecation")
	public void setColor(DyeColor dc)
	{
		if(block.getType().equals(Material.BANNER) || block.getType().equals(Material.WALL_BANNER) || block.getType().equals(Material.STANDING_BANNER))
		{
			Banner banner = (Banner) block.getState();
			banner.setBaseColor(dc);
			banner.update();
			
			return;
		}
		
		new MaterialBlock(block.getType(), dc.getData()).apply(block.getLocation());
	}
	
	/**
	 * Get the color of this block
	 * 
	 * @return the color or null
	 */
	@SuppressWarnings("deprecation")
	public DyeColor getColor()
	{
		if(block.getType().equals(Material.BANNER) || block.getType().equals(Material.WALL_BANNER) || block.getType().equals(Material.STANDING_BANNER))
		{
			Banner banner = (Banner) block.getState();
			return banner.getBaseColor();
		}
		
		return DyeColor.getByData(new MaterialBlock(block.getLocation()).getData());
	}
}

package org.phantomapi.chromatic;

import java.awt.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.phantomapi.Phantom;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.RayTrace;

/**
 * Chromatic utilities
 * 
 * @author cyberpwn
 */
public class Chromatic
{
	/**
	 * Get the given chromatic block
	 * 
	 * @param mb
	 *            the materialblock
	 * @return the chromatic block
	 */
	public static ChromaticBlock getBlock(MaterialBlock mb)
	{
		return Phantom.instance().getBlockUpdateController().getChrome().getBlock(mb);
	}
	
	/**
	 * Is there a chromatic reference for this block type
	 * 
	 * @param mb
	 *            the materialblock
	 * @return true if it does
	 */
	public static boolean hasBlock(MaterialBlock mb)
	{
		return Phantom.instance().getBlockUpdateController().getChrome().hasBlock(mb);
	}
	
	/**
	 * Get the visible color from the location with the given vector direction
	 * with a max trace
	 * 
	 * @param l
	 *            the location of the block in question
	 * @param v
	 *            the vector direction in case of transparency needed for
	 *            blending
	 * @param max
	 *            the max distance to colorblend
	 * @return the visible color or null
	 */
	public static Color getVisibleColor(Location l, Vector v, int max)
	{
		World w = l.getWorld();
		Color[] c = new Color[] {null};
		
		new RayTrace(l, v, (double) max, 1.0)
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onTrace(Location location)
			{
				try
				{
					Block b = w.getBlockAt(location);
					
					if(!b.getType().equals(Material.AIR))
					{
						c[0] = Chromatic.getBlock(new MaterialBlock(b.getType(), b.getData())).getEffectiveColor(b, v);
						stop();
					}
				}
				
				catch(Exception e)
				{
					
				}
			}
		}.trace();
		
		return c[0];
	}
}

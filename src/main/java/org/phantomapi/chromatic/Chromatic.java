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
import org.phantomapi.world.W;

public class Chromatic
{
	public static ChromaticBlock getBlock(MaterialBlock mb)
	{
		return Phantom.instance().getBlockUpdateController().getChrome().getBlock(mb);
	}
	
	public static boolean hasBlock(MaterialBlock mb)
	{
		return Phantom.instance().getBlockUpdateController().getChrome().hasBlock(mb);
	}
	
	public static Color getVisibleColor(Location l, Vector v, int max)
	{
		World w = W.toAsync(l.getWorld());
		Color[] c = new Color[] {null};
		
		new RayTrace(l, v, (double) max, 1.0)
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onTrace(Location location)
			{
				Block b = w.getBlockAt(location);
				
				if(!b.getType().equals(Material.AIR))
				{
					try
					{
						c[0] = Chromatic.getBlock(new MaterialBlock(b.getType(), b.getData())).getEffectiveColor(b, v);
						stop();
					}
					
					catch(Exception e)
					{
						
					}
				}
			}
		}.trace();
		
		return c[0];
	}
}

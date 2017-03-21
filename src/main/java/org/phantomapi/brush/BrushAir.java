package org.phantomapi.brush;

import org.bukkit.Material;
import org.phantomapi.world.VariableBlock;

public class BrushAir extends BrushStatic
{
	public BrushAir()
	{
		super(new VariableBlock(Material.AIR));
	}
}

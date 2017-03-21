package org.phantomapi.brush;

import org.bukkit.Location;
import org.bukkit.Material;
import org.phantomapi.builder.Brush;
import org.phantomapi.util.M;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.VariableBlock;

public class BrushStoneBrick implements Brush
{
	private double grit;
	
	public BrushStoneBrick(double grit)
	{
		this.grit = grit;
	}
	
	@Override
	public VariableBlock brush(Location location)
	{
		VariableBlock vb = new VariableBlock(Material.SMOOTH_BRICK);
		
		if(grit > 0)
		{
			if(M.r(grit))
			{
				vb.addBlock(new MaterialBlock(Material.SMOOTH_BRICK, (byte) M.rand(0, 2)));
			}
		}
		
		return vb;
	}
}

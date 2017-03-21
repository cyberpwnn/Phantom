package org.phantomapi.brush;

import org.bukkit.Location;
import org.phantomapi.builder.Brush;
import org.phantomapi.world.VariableBlock;

public class BrushStatic implements Brush
{
	private VariableBlock basic;
	
	public BrushStatic(VariableBlock basic)
	{
		this.basic = basic;
	}
	
	@Override
	public VariableBlock brush(Location location)
	{
		return basic;
	}
	
}

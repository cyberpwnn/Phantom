package org.phantomapi.builds;

import org.phantomapi.builder.Brush;
import org.phantomapi.builder.Build;
import org.phantomapi.world.Cuboid;
import org.phantomapi.world.WQ;

public class CuboidBuild implements Build
{
	private Cuboid c;
	private boolean fill;
	
	public CuboidBuild(Cuboid c, boolean fill)
	{
		this.c = c;
		this.fill = fill;
	}
	
	@Override
	public void build(WQ q, Brush brush)
	{
		if(fill)
		{
			q.set(c, brush);
		}
		
		else
		{
			q.outline(c, brush);
		}
	}
}

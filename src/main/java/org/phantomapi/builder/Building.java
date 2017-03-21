package org.phantomapi.builder;

import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.WQ;

public class Building implements Build
{
	private GList<Build> builds;
	private GMap<Build, Brush> brushOverride;
	
	public Building()
	{
		builds = new GList<Build>();
		brushOverride = new GMap<Build, Brush>();
	}
	
	public void add(Build build)
	{
		builds.add(build);
	}
	
	public void add(Build build, Brush b)
	{
		builds.add(build);
		overrideBrush(build, b);
	}
	
	public void overrideBrush(Build b, Brush brush)
	{
		brushOverride.put(b, brush);
	}
	
	public void add(GList<Build> build)
	{
		builds.add(build);
	}
	
	public void add(GList<Build> build, Brush b)
	{
		builds.add(build);
		
		for(Build i : build)
		{
			overrideBrush(i, b);
		}
	}
	
	@Override
	public void build(WQ q, Brush brush)
	{
		for(Build i : builds)
		{
			if(brushOverride.containsKey(i))
			{
				i.build(q, brushOverride.get(i));
				continue;
			}
			
			i.build(q, brush);
		}
	}
}

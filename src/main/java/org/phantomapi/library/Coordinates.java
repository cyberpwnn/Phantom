package org.phantomapi.library;

import java.net.MalformedURLException;

public enum Coordinates
{
	WRAITH_CORE("wraith", "core", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	WRAITH_LANG("wraith", "lang", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	WRAITH_MATH("wraith", "math", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	WRAITH_CONSTRUCT("wraith", "construct", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	WRAITH_MINECRAFT("wraith", "minecraft", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	WRAITH_IO("wraith", "io", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	WRAITH_REFLECT("wraith", "reflect", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	WRAITH_CONCURRENT("wraith", "concurrent", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	WRAITH_CLUST("wraith", "clust", WraithAPI.GLOBAL_VERSION, WraithAPI.REPO_VOLMIT),
	GUAVA("com.google.guava", "guava", "21.0", WraithAPI.REPO_CENTRAL),
	COMMON_LANG("org.apache.commons", "commons-lang3", "3.5", WraithAPI.REPO_CENTRAL),
	COMMON_IO("org.apache.commons", "commons-io", "1.3.2", WraithAPI.REPO_CENTRAL),
	COMMON_MATH("org.apache.commons", "commons-math3", "3.5", WraithAPI.REPO_CENTRAL),
	COMMON_COMPRESS("org.apache.commons", "commons-compress", "1.9", WraithAPI.REPO_CENTRAL);
	
	private Coordinate coord;
	
	private Coordinates(String g, String a, String v, String r)
	{
		try
		{
			coord = Coordinate.create(g, a, v, r);
		}
		
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	public Coordinate get()
	{
		return coord;
	}
}

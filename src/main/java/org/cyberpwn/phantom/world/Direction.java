package org.cyberpwn.phantom.world;

import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.world.Cuboid.CuboidDirection;

/**
 * Directions
 * 
 * @author cyberpwn
 *
 */
public enum Direction
{
	U(0, 1, 0, CuboidDirection.Up), D(0, -1, 0, CuboidDirection.Down), N(0, 0, -1, CuboidDirection.North), S(0, 0, 1, CuboidDirection.South), E(1, 0, 0, CuboidDirection.East), W(-1, 0, 0, CuboidDirection.West);
	
	private int x;
	private int y;
	private int z;
	private CuboidDirection f;
	
	private Direction(int x, int y, int z, CuboidDirection f)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.f = f;
	}
	
	public int x()
	{
		return x;
	}
	
	public int y()
	{
		return y;
	}
	
	public int z()
	{
		return z;
	}
	
	public CuboidDirection f()
	{
		return f;
	}
	
	public static GList<Direction> news()
	{
		return new GList<Direction>().qadd(N).qadd(E).qadd(W).qadd(S);
	}
	
	public static GList<Direction> udnews()
	{
		return new GList<Direction>().qadd(U).qadd(D).qadd(N).qadd(E).qadd(W).qadd(S);
	}
}

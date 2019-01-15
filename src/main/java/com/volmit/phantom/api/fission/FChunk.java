package com.volmit.phantom.api.fission;

import org.bukkit.Chunk;
import org.bukkit.World;

public class FChunk
{
	private int x;
	private int z;

	public Chunk toBukkit(World world)
	{
		return world.getChunkAt(x, z);
	}

	public boolean isLoaded(World world)
	{
		return world.isChunkLoaded(x, z);
	}

	public boolean isUsed(World world)
	{
		return world.isChunkInUse(x, z);
	}

	public FChunk(Chunk c)
	{
		this.x = c.getX();
		this.z = c.getZ();
	}

	public FChunk(int x, int z)
	{
		this.x = x;
		this.z = z;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(!(obj instanceof FChunk))
		{
			return false;
		}
		FChunk other = (FChunk) obj;
		if(x != other.x)
		{
			return false;
		}
		if(z != other.z)
		{
			return false;
		}
		return true;
	}
}

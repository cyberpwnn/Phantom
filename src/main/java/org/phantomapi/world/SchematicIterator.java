package org.phantomapi.world;

public class SchematicIterator implements Runnable
{
	private MaterialBlock b;
	private int x;
	private int z;
	private int y;
	
	public void run(MaterialBlock b, int x, int y, int z)
	{
		this.b = b;
		this.x = x;
		this.z = z;
		this.y = y;
		
		run();
	}
	
	@Override
	public void run()
	{
		
	}

	public MaterialBlock getB()
	{
		return b;
	}

	public void setB(MaterialBlock b)
	{
		this.b = b;
	}

	public int getX()
	{
		return x;
	}

	public int getZ()
	{
		return z;
	}

	public int getY()
	{
		return y;
	}
}

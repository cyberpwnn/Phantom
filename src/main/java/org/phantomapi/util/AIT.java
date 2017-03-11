package org.phantomapi.util;

public abstract class AIT
{
	private String name;
	
	public AIT(String name)
	{
		this.name = name;
	}
	
	public abstract void run() throws Exception;

	public String getName()
	{
		return name;
	}
}

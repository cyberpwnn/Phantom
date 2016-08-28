package org.phantomapi.refract;

public class Refraction
{
	protected final Class<?> clazz;
	
	public Refraction(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	public Class<?> getClazz()
	{
		return clazz;
	}
}

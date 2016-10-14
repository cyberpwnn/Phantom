package org.phantomapi.refract;

import java.lang.reflect.Constructor;

public class ConstructorRefraction extends Refraction
{
	protected final Constructor<?> target;
	protected final Object[] parameters;
	
	public ConstructorRefraction(Class<?> clazz, Constructor<?> target, Object... parameters)
	{
		super(clazz);
		
		this.target = target;
		this.parameters = parameters;
	}
	
	public ConstructorRefraction(Class<?> clazz, Constructor<?> target)
	{
		this(clazz, target, null, new Object[0]);
	}

	public Constructor<?> getTarget()
	{
		return target;
	}
	
	public Object[] getParameters()
	{
		return parameters;
	}

	public Object invoke() throws RefractException
	{
		try
		{
			return target.newInstance(parameters);
		}
		
		catch(Exception e)
		{
			throw new RefractException(e.getMessage(), e.getCause());
		}
	}
}

package org.phantomapi.refract;

import java.lang.reflect.Method;

public class MethodRefraction extends Refraction
{
	protected final Method target;
	protected final Object[] parameters;
	protected final Object instance;
	
	public MethodRefraction(Class<?> clazz, Method target, Object instance, Object... parameters)
	{
		super(clazz);
		
		this.target = target;
		this.parameters = parameters;
		this.instance = instance;
	}
	
	public MethodRefraction(Class<?> clazz, Method target, Object... parameters)
	{
		this(clazz, target, null, parameters);
	}
	
	public MethodRefraction(Class<?> clazz, Method target)
	{
		this(clazz, target, null, new Object[0]);
	}
	
	public MethodRefraction(Class<?> clazz, Method target, Object instance)
	{
		this(clazz, target, instance, new Object[0]);
	}

	public Method getTarget()
	{
		return target;
	}
	
	public Object[] getParameters()
	{
		return parameters;
	}

	public Object getInstance()
	{
		return instance;
	}

	public Object invoke() throws RefractException
	{
		try
		{
			return target.invoke(instance, parameters);
		}
		
		catch(Exception e)
		{
			throw new RefractException(e.getMessage(), e.getCause());
		}
	}
}

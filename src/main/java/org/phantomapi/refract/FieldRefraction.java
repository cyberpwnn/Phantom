package org.phantomapi.refract;

import java.lang.reflect.Field;

public class FieldRefraction extends Refraction
{
	protected final Field target;
	protected final Object instance;
	
	public FieldRefraction(Class<?> clazz, Field target, Object instance)
	{
		super(clazz);
		
		this.target = target;
		this.instance = instance;
	}
	
	public FieldRefraction(Class<?> clazz, Field target)
	{
		this(clazz, target, null);
	}

	public Field getTarget()
	{
		return target;
	}	
	
	public Object getInstance()
	{
		return instance;
	}

	public Object get() throws RefractException
	{
		try
		{
			return target.get(instance);
		}
		
		catch(Exception e)
		{
			throw new RefractException(e.getMessage(), e.getCause());
		}
	}
	
	public void set(Object value) throws RefractException
	{
		try
		{
			target.set(instance, value);
		}
		
		catch(Exception e)
		{
			throw new RefractException(e.getMessage(), e.getCause());
		}
	}
}

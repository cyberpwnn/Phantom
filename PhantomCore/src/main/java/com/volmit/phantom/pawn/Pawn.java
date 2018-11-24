package com.volmit.phantom.pawn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GListAdapter;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.plugin.PhantomPlugin;
import com.volmit.phantom.time.M;

public class Pawn
{
	private final UUID id = UUID.randomUUID();
	private GList<Method> methodCache;
	private GList<Field> fieldCache;
	private GMap<Class<? extends Annotation>, GList<Method>> methodTypeCache;

	public Pawn()
	{
		PhantomPlugin.plugin.getPawnManager().insert(this);
	}
	
	public UUID getPawnId()
	{
		return id;
	}

	public void invoke(Class<? extends Annotation> c)
	{
		for(Method i : getAllMethods(c))
		{
			i.setAccessible(true);

			try
			{
				if(c.equals(Tick.class))
				{
					if(M.interval(i.getAnnotation(Tick.class).value()))
					{
						i.invoke(this);
					}
				}
				
				else
				{
					i.invoke(this);
				}
			}

			catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
	}

	public GList<Field> getAllFields()
	{
		if(fieldCache == null)
		{
			fieldCache = new GList<Field>();
		}

		else
		{
			return fieldCache;
		}

		Class<? extends Pawn> p = getClass();
		GList<Class<?>> classes = new GList<Class<?>>().qadd(p);
		GList<Field> fields = new GList<Field>();
		Class<?> c = null;

		while(!(c = p.getSuperclass()).equals(Object.class))
		{
			classes.add(c);
		}

		for(Class<?> i : classes)
		{
			if(i.isAnnotationPresent(Invisible.class))
			{
				continue;
			}

			for(Field j : i.getDeclaredFields())
			{
				if(j.isAnnotationPresent(Invisible.class))
				{
					continue;
				}

				j.setAccessible(true);
				fields.add(j);
			}
		}

		fieldCache.addAll(fields);

		return fields;
	}

	public GList<Method> getAllMethods(Class<? extends Annotation> ac)
	{
		if(methodTypeCache.containsKey(ac))
		{
			return methodTypeCache.get(ac);
		}

		GList<Method> m = new GList<Method>(new GListAdapter<Method, Method>()
		{
			@Override
			public Method onAdapt(Method from)
			{
				if(from.isAnnotationPresent(ac))
				{
					return from;
				}

				return null;
			}
		}.adapt(getAllMethods()));
		methodTypeCache.put(ac, m);

		return m;
	}

	public GList<Method> getAllMethods()
	{
		if(methodCache == null)
		{
			methodCache = new GList<Method>();
		}

		else
		{
			return methodCache;
		}

		Class<? extends Pawn> p = getClass();
		GList<Class<?>> classes = new GList<Class<?>>().qadd(p);
		GList<Method> methods = new GList<Method>();
		Class<?> c = null;

		while(!(c = p.getSuperclass()).equals(Object.class))
		{
			classes.add(c);
		}

		for(Class<?> i : classes)
		{
			if(i.isAnnotationPresent(Invisible.class))
			{
				continue;
			}

			for(Method j : i.getDeclaredMethods())
			{
				if(j.isAnnotationPresent(Invisible.class))
				{
					continue;
				}

				j.setAccessible(true);
				methods.add(j);
			}
		}

		methodCache.addAll(methods);

		return methods;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Pawn other = (Pawn) obj;
		if(id == null)
		{
			if(other.id != null)
				return false;
		}
		else
			if(!id.equals(other.id))
				return false;
		return true;
	}
}

package com.volmit.phantom.imp.module;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import java.util.function.Function;

public class SeekableObject
{
	public int invokeEach(AnnotationSeeker seeker, Consumer<Method> methodConsumer)
	{
		int m = 0;

		for(Method i : seeker.seekMethods(getClass()))
		{
			try
			{
				i.setAccessible(true);
				methodConsumer.accept(i);
				m++;
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}

		return m;
	}

	public int invokeAll(AnnotationSeeker seeker, Object... pars)
	{
		return invokeEach(seeker, (m) ->
		{
			try
			{
				m.invoke(Modifier.isStatic(m.getModifiers()) ? null : this, pars);
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		});
	}

	public boolean invokeSingle(AnnotationSeeker seeker, String name, Object... pars)
	{
		for(Method i : seeker.seekMethods(getClass()))
		{
			if(i.getName().equals(name))
			{
				try
				{
					i.setAccessible(true);
					i.invoke(Modifier.isStatic(i.getModifiers()) ? null : this, pars);
					return true;
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public int setAll(AnnotationSeeker seeker, Object value)
	{
		return setEach(seeker, (f) -> value);
	}

	public int setEach(AnnotationSeeker seeker, Function<Field, Object> f)
	{
		int m = 0;

		for(Field i : seeker.seekFields(getClass()))
		{
			try
			{
				i.setAccessible(true);
				i.set(Modifier.isStatic(i.getModifiers()) ? null : this, f.apply(i));
				m++;
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}

		return m;
	}
}

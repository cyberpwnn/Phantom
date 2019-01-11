package com.volmit.phantom.api.module;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.volmit.phantom.api.lang.GList;

public class AnnotationSeeker
{
	private final Class<? extends Annotation> annotationType;
	private final ModifierAwareSeeker seeker;

	public AnnotationSeeker(Class<? extends Annotation> annotationType, ModifierAwareSeeker seeker)
	{
		this.annotationType = annotationType;
		this.seeker = seeker;
	}

	public GList<Field> seekFields(Class<?> clazz)
	{
		GList<Field> fields = new GList<>();

		for(Field i : clazz.getDeclaredFields())
		{
			i.setAccessible(true);

			if(i.isAnnotationPresent(annotationType) && seeker.isValidModifiers(i.getModifiers()))
			{
				fields.add(i);
			}
		}

		return fields;
	}

	public GList<Method> seekMethods(Class<?> clazz)
	{
		GList<Method> fields = new GList<>();

		for(Method i : clazz.getDeclaredMethods())
		{
			i.setAccessible(true);

			if(i.isAnnotationPresent(annotationType) && seeker.isValidModifiers(i.getModifiers()))
			{
				fields.add(i);
			}
		}

		return fields;
	}

	public Class<? extends Annotation> getAnnotationType()
	{
		return annotationType;
	}
}

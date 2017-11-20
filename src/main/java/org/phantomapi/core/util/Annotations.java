package org.phantomapi.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.cyberpwn.glang.GMap;

/**
 * Utilities for reading annotations
 *
 * @author cyberpwn
 *
 */
public class Annotations
{
	/**
	 * Get a mapping of every field against it's annotation specified by
	 * annotationClass
	 *
	 * @param object
	 *            Accepts Class or and Object which is assumed Object.getClass()
	 * @param annotationClass
	 *            the annotation class to grab from fields
	 * @return a mapping of every field with the given annotation, and it's
	 *         annotation instance as a value
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> GMap<Field, A> getAnnotatedFields(Object object, Class<? extends Annotation> annotationClass)
	{
		GMap<Field, A> mapping = new GMap<Field, A>();
		Class<?> clazz = object instanceof Class<?> ? (Class<?>) object : object.getClass();

		for(Field i : clazz.getDeclaredFields())
		{
			if(hasAnnotation(i, annotationClass))
			{
				mapping.put(i, (A) getAnnotation(i, annotationClass));
			}
		}

		return mapping;
	}

	/**
	 * Get a mapping of every method against it's annotation specified by
	 * annotationClass
	 *
	 * @param object
	 *            Accepts Class or and Object which is assumed Object.getClass()
	 * @param annotationClass
	 *            the annotation class to grab from methods
	 * @return a mapping of every method with the given annotation, and it's
	 *         annotation instance as a value
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> GMap<Method, A> getAnnotatedMethods(Object object, Class<? extends Annotation> annotationClass)
	{
		GMap<Method, A> mapping = new GMap<Method, A>();
		Class<?> clazz = object instanceof Class<?> ? (Class<?>) object : object.getClass();

		for(Method i : clazz.getDeclaredMethods())
		{
			if(hasAnnotation(i, annotationClass))
			{
				mapping.put(i, (A) getAnnotation(i, annotationClass));
			}
		}

		return mapping;
	}

	/**
	 * Checks if the given object has an annotation.
	 *
	 * @param object
	 *            Accepts Classes, Fields and Methods. If another type of object is
	 *            passed, the class of that object is assumed.
	 * @param annotationClass
	 *            the class of the checking annotation
	 * @return returns true of the object has the given annotation
	 */
	public static boolean hasAnnotation(Object object, Class<? extends Annotation> annotationClass)
	{
		return getAnnotation(object, annotationClass) != null;
	}

	/**
	 * Gets the annotation for the given object.
	 *
	 * @param object
	 *            Accepts Classes, Fields and Methods. If another type of object is
	 *            passed, the class of that object is assumed.
	 * @param annotationClass
	 *            the class of the annotation to return
	 * @return returns the annotation object, or null if not found
	 */
	public static <A extends Annotation> A getAnnotation(Object object, Class<A> annotationClass)
	{
		if(object instanceof Field)
		{
			return ((Field) object).getDeclaredAnnotation(annotationClass);
		}

		if(object instanceof Method)
		{
			return ((Method) object).getDeclaredAnnotation(annotationClass);
		}

		if(object instanceof Class<?>)
		{
			return ((Class<?>) object).getDeclaredAnnotation(annotationClass);
		}

		return getAnnotation(object.getClass(), annotationClass);
	}
}

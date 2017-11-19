package org.phantomapi.core;

import java.io.File;
import java.io.IOException;

import org.cyberpwn.glang.GList;
import org.cyberpwn.glang.GListAdapter;

import phantom.annotation.Controller;
import phantom.annotation.MasterController;
import phantom.util.annotations.Annotations;
import phantom.util.files.Archives;

/**
 * The gateway beteen the world of phantom and the underlying api
 *
 * @author cyberpwn
 *
 */
public interface ICorePlugin
{
	public GList<Class<?>> getControllerClasses();

	public GList<Class<?>> getMasterControllerClasses();

	public void enableController(Object o) throws Exception;

	public void disableController(Object o) throws Exception;

	public void onInit();

	public void onEnable();

	public void onDisable();

	public void onLoad();

	public void onTickSync();

	public void onTickAsync();

	/**
	 * Initialize an object
	 *
	 * @param clazz
	 *            the class
	 * @return the object
	 * @throws Exception
	 *             shit happens
	 */
	public static Object initializeObject(Class<?> clazz) throws Exception
	{
		return clazz.getConstructor().newInstance();
	}

	/**
	 * Get all master controllers from controllers
	 *
	 * @param classes
	 *            the classes
	 * @return master controller classes
	 */
	public static GList<Class<?>> getMasterControllers(GList<Class<?>> classes)
	{
		return (GList<Class<?>>) new GListAdapter<Class<?>, Class<?>>()
		{
			@Override
			public Class<?> onAdapt(Class<?> from)
			{
				return Annotations.hasAnnotation(from, MasterController.class) ? from : null;
			}
		}.adapt(classes);
	}

	/**
	 * Get all classes which are annotated as controllers
	 *
	 * @param classes
	 *            the list of classes to check for
	 * @return The list of classes which are controllers
	 */
	public static GList<Class<?>> getControllerClasses(GList<Class<?>> classes)
	{
		return (GList<Class<?>>) new GListAdapter<Class<?>, Class<?>>()
		{
			@Override
			public Class<?> onAdapt(Class<?> from)
			{
				return Annotations.hasAnnotation(from, Controller.class) ? from : null;
			}
		}.adapt(classes);
	}

	/**
	 * Get all classes in a jar
	 *
	 * @param jar
	 *            the jar file
	 * @return a glist of classes
	 * @throws IOException
	 *             shit happens
	 */
	public static GList<Class<?>> getClassesFromJar(File jar) throws IOException
	{
		return (GList<Class<?>>) new GListAdapter<String, Class<?>>()
		{
			@Override
			public Class<?> onAdapt(String i)
			{
				try
				{
					return i.endsWith(".class") && !i.contains("$") ? Class.forName(i.replaceAll("/", ".").substring(0, i.length() - 6)) : null;
				}

				catch(Throwable e)
				{
					return null;
				}
			}
		}.adapt(new GList<String>(Archives.listFiles(jar)));
	}
}

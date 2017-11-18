package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.cyberpwn.gconcurrent.A;
import org.cyberpwn.gconcurrent.ParallelPoolManager;
import org.cyberpwn.gconcurrent.QueueMode;
import org.cyberpwn.gconcurrent.S;
import org.cyberpwn.gformat.F;
import org.cyberpwn.glang.GList;
import org.cyberpwn.glog.L;

import phantom.annotation.Control;
import phantom.annotation.Controller;
import phantom.annotation.Disable;
import phantom.annotation.Enable;
import phantom.annotation.Instance;
import phantom.annotation.MasterController;
import phantom.util.annotations.Annotations;

public class CorePlugin implements ICorePlugin
{
	private ParallelPoolManager pool;
	private GList<Object> controllers;
	private GList<Class<?>> cControllers;
	private GList<Class<?>> cMasterControllers;
	private GList<Class<?>> classes;
	private GList<File> searchDirectories;

	public CorePlugin(GList<File> searchDirectories) throws IOException
	{
		this.searchDirectories = searchDirectories;
		controllers = new GList<Object>();
		cControllers = new GList<Class<?>>();
		cMasterControllers = new GList<Class<?>>();
		classes = new GList<Class<?>>();
		pool = new ParallelPoolManager("Ghost", 4, QueueMode.ROUND_ROBIN)
		{
			@Override
			public long getNanoGate()
			{
				return 100000000;
			}
		};

		pool.start();
		A.mgr = pool;
		S.mgr = pool;
	}

	private Object initializeMasterController(Class<?> c) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Object inst = c.getConstructor().newInstance();

		for(Field i : Annotations.getAnnotatedFields(c, Control.class).k())
		{
			if(Modifier.isStatic(i.getModifiers()))
			{
				L.f("  STATIC @Control field in " + c.getSimpleName());
				return inst;
			}

			if(Modifier.isFinal(i.getModifiers()))
			{
				L.f("  FINAL @Control field in " + c.getSimpleName());
				return inst;
			}

			i.setAccessible(true);
			i.set(inst, initializeController(i.getType()));
		}

		return inst;
	}

	private Object initializeController(Class<?> c) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Object inst = c.getConstructor().newInstance();

		for(Field i : Annotations.getAnnotatedFields(c, Control.class).k())
		{
			if(Modifier.isStatic(i.getModifiers()))
			{
				L.f("  STATIC @Control field in " + c.getSimpleName());
				return inst;
			}

			if(Modifier.isFinal(i.getModifiers()))
			{
				L.f("  FINAL @Control field in " + c.getSimpleName());
				return inst;
			}

			i.setAccessible(true);
			i.set(inst, initializeController(i.getType()));
		}

		return inst;
	}

	public CorePlugin(File... files) throws IOException
	{
		this(new GList<File>(files));
	}

	@Override
	public GList<Class<?>> getControllerClasses()
	{
		return cControllers;
	}

	@Override
	public GList<Class<?>> getMasterControllerClasses()
	{
		return cMasterControllers;
	}

	@Override
	public void onTick()
	{
		pool.tickSyncQueue();
	}

	@Override
	public void onInit()
	{

	}

	@Override
	public void onEnable()
	{
		for(Object i : controllers)
		{
			try
			{
				enableController(i);
			}

			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDisable()
	{
		pool.shutdown();

		for(Object i : controllers)
		{
			try
			{
				disableController(i);
			}

			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLoad()
	{
		for(File i : searchDirectories)
		{
			for(File j : i.listFiles())
			{
				if(j.getName().endsWith(".jar"))
				{
					int s = classes.size();

					try
					{
						classes.addAll(ICorePlugin.getClassesFromJar(j));
					}

					catch(IOException e)
					{
						e.printStackTrace();
					}

					L.l("  Reading " + F.f(classes.size() - s) + " classes from " + j.getName());
				}
			}
		}

		L.l("Read " + F.f(classes.size()));
		L.l("Processing " + F.f(classes.size()) + " classes");

		for(Class<?> i : classes)
		{
			Class<?> clazz = i;

			if(Annotations.hasAnnotation(clazz, MasterController.class))
			{
				L.l("  @MasterController " + clazz.getSimpleName());
				cMasterControllers.add(clazz);
			}

			else if(Annotations.hasAnnotation(clazz, Controller.class))
			{
				L.l("  @Controller " + clazz.getSimpleName());
				cControllers.add(clazz);
			}
		}

		L.l("Processed " + F.f(classes.size()) + " classes");
		L.l("Initializing Master Controllers");

		for(Class<?> i : cMasterControllers)
		{
			Class<?> c = i;

			try
			{
				Object o = initializeMasterController(c);

				if(o != null)
				{
					L.f("  Initialized Master Controller " + o.getClass().getSimpleName());

					for(Field j : Annotations.getAnnotatedFields(o, Instance.class).k())
					{
						j.setAccessible(true);
						j.set(o, o);
					}

					controllers.add(o);
				}
			}

			catch(Throwable e)
			{
				L.f("  Failed to initialize Master controller ");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void enableController(Object o) throws Exception
	{
		for(Field i : Annotations.getAnnotatedFields(o, Control.class).k())
		{
			i.setAccessible(true);

			if(i.get(o) != null)
			{
				enableController(i.get(o));
			}
		}

		for(Method i : Annotations.getAnnotatedMethods(o, Enable.class).k())
		{
			i.setAccessible(true);
			i.invoke(o);
		}
	}

	@Override
	public void disableController(Object o) throws Exception
	{
		for(Field i : Annotations.getAnnotatedFields(o, Control.class).k())
		{
			i.setAccessible(true);

			if(i.get(o) != null)
			{
				disableController(i.get(o));
			}
		}

		for(Method i : Annotations.getAnnotatedMethods(o, Disable.class).k())
		{
			i.setAccessible(true);
			i.invoke(o);
		}
	}
}

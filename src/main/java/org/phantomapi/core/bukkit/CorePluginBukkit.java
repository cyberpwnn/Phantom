package org.phantomapi.core.bukkit;

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
import org.cyberpwn.gconcurrent.TICK;
import org.cyberpwn.gformat.F;
import org.cyberpwn.glang.GBiset;
import org.cyberpwn.glang.GList;
import org.cyberpwn.glang.GMap;
import org.cyberpwn.glog.L;
import org.phantomapi.core.ICorePlugin;
import org.phantomapi.core.IGateway;
import org.phantomapi.core.Phantom;
import org.phantomapi.core.util.Annotations;

import phantom.annotation.Async;
import phantom.annotation.Control;
import phantom.annotation.Controller;
import phantom.annotation.Disable;
import phantom.annotation.Enable;
import phantom.annotation.Instance;
import phantom.annotation.MasterController;
import phantom.annotation.Tick;

public class CorePluginBukkit implements ICorePlugin
{
	private ParallelPoolManager pool;
	private GList<Object> controllers;
	private GList<Class<?>> cControllers;
	private GList<Class<?>> cMasterControllers;
	private GList<Class<?>> classes;
	private GList<File> searchDirectories;
	private GMap<GBiset<Object, Method>, Integer> syncTick;
	private GMap<GBiset<Object, Method>, Integer> asyncTick;
	private IGateway gateway;

	public CorePluginBukkit(GList<File> searchDirectories) throws Exception
	{
		this.searchDirectories = searchDirectories;
		syncTick = new GMap<GBiset<Object, Method>, Integer>();
		asyncTick = new GMap<GBiset<Object, Method>, Integer>();
		controllers = new GList<Object>();
		cControllers = new GList<Class<?>>();
		cMasterControllers = new GList<Class<?>>();
		classes = new GList<Class<?>>();
		Field coreF = Phantom.class.getField("core");
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
		coreF.setAccessible(true);
		coreF.set(null, this);
		gateway = new BukkitGateway();
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

		for(Method i : Annotations.getAnnotatedMethods(c, Tick.class).k())
		{
			if(Annotations.hasAnnotation(c, Async.class))
			{
				asyncTick.put(new GBiset<Object, Method>(inst, i), Annotations.getAnnotation(i, Tick.class).value());
			}

			else
			{
				syncTick.put(new GBiset<Object, Method>(inst, i), Annotations.getAnnotation(i, Tick.class).value());
			}
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

		for(Method i : Annotations.getAnnotatedMethods(c, Tick.class).k())
		{
			if(Annotations.hasAnnotation(c, Async.class))
			{
				asyncTick.put(new GBiset<Object, Method>(inst, i), Annotations.getAnnotation(i, Tick.class).value());
			}

			else
			{
				syncTick.put(new GBiset<Object, Method>(inst, i), Annotations.getAnnotation(i, Tick.class).value());
			}
		}

		return inst;
	}

	public CorePluginBukkit(File... files) throws Exception
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
	public void onInit()
	{

	}

	@Override
	public void onEnable()
	{
		new A()
		{
			@Override
			public void run()
			{
				readClasses();
				processClasses();

				new S()
				{
					@Override
					public void run()
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
				};
			}
		};
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

	}

	private void processClasses()
	{
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
			processClass(i);
		}
	}

	private void processClass(Class<?> c)
	{
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

	private void readClasses()
	{
		for(File i : searchDirectories)
		{
			readSeachDirectory(i);
		}

		L.l("Read " + F.f(classes.size()));
	}

	private void readSeachDirectory(File i)
	{
		for(File j : i.listFiles())
		{
			readJar(j);
		}
	}

	private void readJar(File j)
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
			if(Annotations.hasAnnotation(i, Async.class))
			{
				new A()
				{
					@Override
					public void run()
					{
						i.setAccessible(true);

						try
						{
							i.invoke(o);
						}

						catch(Throwable e)
						{
							e.printStackTrace();
						}
					}
				};
			}

			else
			{
				i.setAccessible(true);
				i.invoke(o);
			}
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
			if(Annotations.hasAnnotation(i, Async.class))
			{
				new A()
				{
					@Override
					public void run()
					{
						i.setAccessible(true);

						try
						{
							i.invoke(o);
						}

						catch(Throwable e)
						{
							e.printStackTrace();
						}
					}
				};
			}

			else
			{
				i.setAccessible(true);
				i.invoke(o);
			}
		}
	}

	@Override
	public void onTickSync()
	{
		pool.tickSyncQueue();

		for(GBiset<Object, Method> m : syncTick.k())
		{
			if(TICK.tick % (syncTick.get(m) < 1 ? 1 : syncTick.get(m)) == 0)
			{
				try
				{
					m.getB().invoke(m.getA());
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onTickAsync()
	{
		for(GBiset<Object, Method> m : asyncTick.k())
		{
			if(TICK.tick % (asyncTick.get(m) < 1 ? 1 : asyncTick.get(m)) == 0)
			{
				try
				{
					m.getB().invoke(m.getA());
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public IGateway getGateway()
	{
		return gateway;
	}
}

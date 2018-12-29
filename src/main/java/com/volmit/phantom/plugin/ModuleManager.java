package com.volmit.phantom.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.UUID;

import com.volmit.phantom.lang.F;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.JarScanner;
import com.volmit.phantom.lang.Profiler;

public class ModuleManager
{
	private GMap<File, ClassLoader> fileLock;
	private GMap<File, Class<?>> fileLockClasses;
	private GList<Class<?>> moduleCache;
	private GMap<String, Module> modules;
	private GMap<UUID, StructuredModule> moduleStructure;
	private File moduleFolder;

	public ModuleManager()
	{
		modules = new GMap<>();
		moduleStructure = new GMap<>();
		fileLock = new GMap<>();
		fileLockClasses = new GMap<>();
		moduleCache = new GList<>();
		moduleFolder = new File("modules");
		moduleFolder.mkdirs();
	}

	public void start()
	{
		searchForModules();
		loadModules();
		startModules();
	}

	private void startModules()
	{
		System.out.println(moduleStructure.size() + "");

		for(String i : modules.k())
		{
			startModule(modules.get(i));
		}
	}

	protected void stopModules()
	{
		for(String i : modules.k())
		{
			stopModule(modules.get(i));
		}
	}

	public void startModule(Module module)
	{
		Profiler px = new Profiler();
		px.begin();
		module.getStructure().start();
		px.end();
		module.getStructure().getDispatcher().l("Started in " + F.time(px.getMilliseconds(), 1));
	}

	public void stopModule(Module module)
	{
		Profiler px = new Profiler();
		px.begin();
		module.getStructure().stop();
		px.end();
		module.getStructure().getDispatcher().l("Stopped in " + F.time(px.getMilliseconds(), 1));
	}

	public void reloadModule(Module module)
	{
		Profiler px = new Profiler();
		px.begin();
		stopModule(module);
		startModule(module);
		px.end();
		module.getStructure().getDispatcher().l("Reloaded in " + F.time(px.getMilliseconds(), 1));
	}

	public void reinjectModule(Module module) throws Throwable
	{
		File f = fileForModule(module);
		unloadModule(module);
		startModule(loadModule(f));
	}

	private Module loadModule(File module) throws Throwable
	{
		try
		{
			return loadModule(scanJar(module));
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private void unloadModule(Module module) throws IOException
	{
		stopModule(module);
		File f = fileForModule(module);
		String name = module.getName();
		modules.remove(name);
		moduleStructure.remove(module.INSTANCE_ID);
		ClassLoader c = fileLock.get(f);
		Class<?> clazz = fileLockClasses.get(f);
		fileLock.remove(f);
		fileLockClasses.remove(f);
		moduleCache.remove(clazz);
		((URLClassLoader) c).close();
		System.gc();
	}

	private File fileForModule(Module module)
	{
		for(File i : fileLockClasses.k())
		{
			if(fileLockClasses.get(i).equals(module.getClass()))
			{
				return i;
			}
		}

		return null;
	}

	private Module loadModule(Class<?> i) throws Throwable
	{
		Profiler p = new Profiler();
		p.begin();
		Module m = (Module) i.getConstructor().newInstance();
		StructuredModule s = new StructuredModule(m);
		moduleStructure.put(m.INSTANCE_ID, s);
		modules.put(s.getInfo().name(), m);
		p.end();
		s.getDispatcher().l("Loaded in " + F.time(p.getMilliseconds(), 1));
		return m;
	}

	private void loadModules()
	{
		for(Class<?> i : moduleCache)
		{
			try
			{
				loadModule(i);
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	public void searchForModules()
	{
		for(File i : moduleFolder.listFiles())
		{
			if(i.isFile() && i.getName().endsWith(".jar"))
			{
				try
				{
					scanJar(i);
				}

				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private Class<?> scanJar(File f) throws IOException
	{
		Class<?> cx = null;
		ClassLoader cl = new URLClassLoader(new URL[] {f.toURL()}, getClass().getClassLoader());
		JarScanner js = new JarScanner(f, cl);
		js.scan();

		for(Class<?> i : js.getClasses())
		{
			if(Module.class.isAssignableFrom(i))
			{
				moduleCache.add(i);
				fileLockClasses.put(f, i);
				cx = i;
			}
		}

		fileLock.put(f, cl);

		return cx;
	}

	public StructuredModule getStructure(Module module)
	{
		return moduleStructure.get(module.INSTANCE_ID);
	}

	public File getDataFolder(Module phantomModule)
	{
		return new File(moduleFolder, phantomModule.getName());
	}
}

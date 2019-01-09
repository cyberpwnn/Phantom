package com.volmit.phantom.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.F;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.JarScanner;
import com.volmit.phantom.lang.Profiler;
import com.volmit.phantom.util.PluginUtil;

public class ModuleManager
{
	private GMap<File, ClassLoader> fileLock;
	private GMap<File, Class<?>> fileLockClasses;
	private GList<Class<?>> moduleCache;
	private GMap<String, Module> modules;
	private GMap<UUID, StructuredModule> moduleStructure;
	private GMap<File, GList<Class<?>>> moduleClasses;
	private File moduleFolder;

	public ModuleManager()
	{
		modules = new GMap<>();
		moduleStructure = new GMap<>();
		fileLock = new GMap<>();
		fileLockClasses = new GMap<>();
		moduleCache = new GList<>();
		moduleClasses = new GMap<>();
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
		Phantom.afterStartup(new Runnable()
		{
			@Override
			public void run()
			{
				startModuleNow(module);
			}
		});
	}

	public Module startModuleNow(Module module)
	{
		Profiler px = new Profiler();
		px.begin();
		module.getStructure().start();
		px.end();
		module.getStructure().getDispatcher().l("Started in " + F.time(px.getMilliseconds(), 1));
		return module;
	}

	public void stopModule(Module module)
	{
		Profiler px = new Profiler();
		px.begin();
		module.getStructure().stop();
		px.end();
		stopModuleServices(module);
		module.getStructure().getDispatcher().l("Stopped in " + F.time(px.getMilliseconds(), 1));
	}

	private void stopModuleServices(Module module)
	{
		for(Class<? extends IService> i : Phantom.getRunningServices())
		{
			Module m = getModuleFromJarClass(i);
			if(m != null && m.getName().equals(module.getName()))
			{
				D.as("Service Manager").l("Stopping Service: " + m.getName() + "/" + i.getSimpleName());
				Phantom.stopService(i);
			}
		}
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

	public Module loadModule(File module) throws Throwable
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

	public void unloadModule(Module module) throws IOException
	{
		unloadModule(module, true);
	}

	public Module getModuleFromJarClass(Class<?> c)
	{
		for(File i : moduleClasses.k())
		{
			for(Class<?> j : moduleClasses.get(i))
			{
				try
				{
					if(j.toString().equals(c.toString()) || j.toString().contains(c.toString()) || c.toString().contains(j.toString()))
					{
						for(Module k : getModules())
						{
							if(k.getModuleFile() != null && k.getModuleFile().equals(i))
							{
								return k;
							}
						}
					}
				}

				catch(Throwable e)
				{

				}
			}
		}

		return null;
	}

	public void unloadModule(Module module, boolean gc) throws IOException
	{
		stopModule(module);
		File f = fileForModule(module);
		String name = module.getName();

		for(Class<? extends IService> i : Phantom.getRunningServices())
		{
			File fx = new File(i.getProtectionDomain().getCodeSource().getLocation().getFile());

			if(fx.equals(f))
			{
				D.as("Phantom > Service Provider").w("Stopping Service (owned by " + module.getName() + ") due to unload.");
				Phantom.stopService(i);
			}
		}

		for(PhantomCommand i : module.getStructure().getCommands())
		{
			module.unregisterCommand(i);
		}

		module.getStructure().getCommands().clear();
		module.getStructure().getActions().clear();
		modules.remove(name);
		moduleStructure.remove(module.INSTANCE_ID);
		ClassLoader c = fileLock.get(f);
		Class<?> clazz = fileLockClasses.get(f);
		fileLock.remove(f);
		fileLockClasses.remove(f);
		moduleCache.remove(clazz);
		((ModuleClassLoader) c).close();

		if(gc)
		{
			System.gc();
		}
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
		File xf = null;

		for(File f : fileLockClasses.k())
		{
			if(fileLockClasses.get(f).equals(i))
			{
				xf = f;
				break;
			}
		}

		Profiler p = new Profiler();
		p.begin();
		Module m = (Module) i.getConstructor().newInstance();
		StructuredModule s = new StructuredModule(m, xf);
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
			if(i.equals(Module.class))
			{
				continue;
			}

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
		try
		{
			scanJar(PluginUtil.getPluginFile(PhantomPlugin.plugin));
		}

		catch(IOException e1)
		{
			e1.printStackTrace();
		}

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

	private Class<?> scanJar(File f) throws IOException
	{
		Class<?> cx = null;
		ClassLoader cl = new ModuleClassLoader(f, new URL[] {f.toURI().toURL()}, getClass().getClassLoader());
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

		if(cx != null)
		{
			fileLock.put(f, cl);
			moduleClasses.put(f, new GList<>());

			for(Class<?> i : js.getClasses())
			{
				moduleClasses.get(f).add(i);
			}
		}

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

	public File getDataFolder()
	{
		moduleFolder.mkdirs();
		return moduleFolder;
	}

	public GList<Module> getModules()
	{
		return modules.v();
	}

	public void unloadModules()
	{
		for(Module i : getModules())
		{
			if(i.getModuleFile() == null)
			{
				D.as("Module Manager").w("Cannot identify module " + i.getClass().getSimpleName() + " file origin. Not unloading.");
				continue;
			}

			if(!i.getModuleFile().equals(PluginUtil.getPluginFile(PhantomPlugin.plugin)))
			{
				try
				{
					unloadModule(i);
				}

				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		System.gc();
		modules.clear();
		moduleCache.clear();
		fileLock.clear();
		fileLockClasses.clear();
		moduleStructure.clear();
		Phantom.flushLogBuffer();
	}

	public int getLoadedClassCount(Module i)
	{
		if(i.getModuleFile() != null)
		{
			return moduleClasses.get(i.getModuleFile()).size();
		}

		return -1;
	}

	public int getRunningServices(Module mod)
	{
		int mx = 0;

		for(Class<? extends IService> i : Phantom.getRunningServices())
		{
			Module m = getModuleFromJarClass(i);

			if(m != null && m.getName().equals(mod.getName()))
			{
				mx++;
			}
		}

		return mx;
	}
}

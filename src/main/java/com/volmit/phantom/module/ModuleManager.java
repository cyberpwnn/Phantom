package com.volmit.phantom.module;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.bukkit.Bukkit;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.JarScanner;
import com.volmit.phantom.pawn.Module;
import com.volmit.phantom.pawn.Pawn;
import com.volmit.phantom.pawn.Stop;

public class ModuleManager
{
	private GMap<String, Pawn> modules;
	private GMap<String, ClassLoader> files;
	private File moduleFolder;

	public ModuleManager()
	{
		modules = new GMap<String, Pawn>();
		files = new GMap<String, ClassLoader>();
		moduleFolder = new File("modules");
		moduleFolder.mkdirs();
	}

	public void start()
	{
		for(File i : moduleFolder.listFiles())
		{
			try
			{
				start(i);
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	public void start(File f) throws MalformedURLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		@SuppressWarnings("deprecation")
		URLClassLoader child = new URLClassLoader(new URL[]
		{f.toURL()}, Bukkit.class.getClassLoader());
		JarScanner js = new JarScanner(f, child);

		for(Class<?> i : js.getClasses())
		{
			if(i.isAssignableFrom(Pawn.class) && i.isAnnotationPresent(Module.class))
			{
				start((Pawn) i.getConstructor().newInstance(), child);
				break;
			}
		}
	}

	public void start(Pawn pawn, ClassLoader child)
	{
		modules.put(pawn.getModuleId(), pawn);
		files.put(pawn.getModuleId(), child);
	}

	public void stop(String id) throws IOException
	{
		modules.get(id).invoke(Stop.class);
		modules.remove(id);
		((URLClassLoader) files.get(id)).close();
	}
}

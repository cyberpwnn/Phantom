package com.volmit.phantom.imp.module;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.volmit.phantom.api.job.J;
import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.lang.GSet;
import com.volmit.phantom.api.lang.JarScanner;
import com.volmit.phantom.api.module.Module;
import com.volmit.phantom.api.module.ModuleDescription;
import com.volmit.phantom.api.module.ModuleOperation;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.lib.service.DevelopmentSVC;
import com.volmit.phantom.main.Phantom;
import com.volmit.phantom.main.PhantomModule;

public class ModuleManager
{
	private final File moduleFolder;
	private final GMap<File, URLClassLoader> fileLocks;
	private final GMap<File, GSet<Class<?>>> classes;
	private final GMap<File, Module> fileModules;

	public ModuleManager()
	{
		moduleFolder = new File("modules");
		fileLocks = new GMap<>();
		classes = new GMap<>();
		fileModules = new GMap<>();
		moduleFolder.mkdirs();

		File inject = new File(moduleFolder, "inject");

		if(inject.exists() && inject.isDirectory())
		{
			D.ll("Found inject folder. Starting Developer SVC");
			SVC.start(DevelopmentSVC.class);
		}

		J.asa(() ->
		{
			try
			{
				PhantomModule.create();
			}

			catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}

			for(File i : moduleFolder.listFiles())
			{
				if(i.isFile() && i.getName().endsWith(".jar"))
				{
					loadModule(i);
				}
			}
		});
	}

	public File getModuleFolder()
	{
		return moduleFolder;
	}

	public void reloadModule(Module module)
	{
		File f = module.getModuleFile();
		unloadModule(module, () -> loadModule(f));
	}

	public void unloadModule(Module module, Runnable onUnloaded)
	{
		J.asa(() ->
		{
			J.s(() ->
			{
				try
				{
					module.executeModuleOperation(ModuleOperation.STOP);
					stopModuleServices(module);
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}

				J.a(() ->
				{
					try
					{
						try
						{
							module.executeModuleOperation(ModuleOperation.UNREGISTER_ALL);
						}

						catch(Throwable e)
						{
							e.printStackTrace();
						}

						File f = module.getModuleFile();
						fileModules.remove(f);
						classes.remove(f);
						fileLocks.get(f).close();
						fileLocks.remove(f);
						System.gc();
						onUnloaded.run();
					}

					catch(IOException e)
					{
						e.printStackTrace();
					}
				});
			});
		});
	}

	private void stopModuleServices(Module module)
	{
		for(Class<? extends IService> i : module.getRegisteredServices())
		{
			if(SVC.isRunning(i))
			{
				Phantom.stopService(i);
			}
		}
	}

	public void loadModule(File file)
	{
		J.asa(() ->
		{
			Module mm = null;

			try
			{
				URLClassLoader cl = new URLClassLoader(new URL[] {file.toURI().toURL()}, getClass().getClassLoader());
				JarScanner js = new JarScanner(file, cl);
				js.scan();
				js.scanForPomProperties();
				fileLocks.put(file, cl);
				classes.put(file, js.getClasses());
				Class<? extends Module> mclass = findModuleClass(file);
				assertNotNull("Could not find module class. " + file.getPath(), mclass);
				Module m = mclass.getConstructor().newInstance();
				mm = m;
				fileModules.put(file, m);
				String pom = findPom(mclass.getSimpleName(), js);
				assertNotNull("Could not find module pom, module class name should match pom artifactId and Name", pom);
				ModuleDescription desc = buildDescription(pom);
				if(m.getClass().isAnnotationPresent(com.volmit.phantom.api.module.Color.class))
				{
					desc.setColor(m.getClass().getAnnotation(com.volmit.phantom.api.module.Color.class).value());

					if(!desc.getColor().isColor())
					{
						throw new RuntimeException("Color tag cannot be a format type!");
					}
				}

				assertNotNull("Could not read pom file.", desc);
				Field fd = Module.class.getDeclaredField("description");
				Field ff = Module.class.getDeclaredField("moduleFile");
				fd.setAccessible(true);
				ff.setAccessible(true);
				fd.set(m, desc);
				ff.set(m, file);
				m.executeModuleOperation(ModuleOperation.REGISTER_INSTANCES);
				m.executeModuleOperation(ModuleOperation.REGISTER_PERMISSIONS);
				m.executeModuleOperation(ModuleOperation.REGISTER_COMMANDS);
				m.executeModuleOperation(ModuleOperation.REGISTER_CONFIGS);
				m.executeModuleOperation(ModuleOperation.REGISTER_SERVICES);
				J.s(() -> m.executeModuleOperation(ModuleOperation.START));
				D.ll("Started Module: " + m.getName());
			}

			catch(Throwable e)
			{
				e.printStackTrace();
				D.ff("Failed to start module. Cleaning up...");

				try
				{
					if(mm != null)
					{
						mm.executeModuleOperation(ModuleOperation.UNREGISTER_ALL);
					}
				}

				catch(Throwable ez)
				{
					D.ww("Failed to properly unregister objects from a failing-start module. This may cause issues when it is loaded again.");
				}

				fileModules.remove(file);
				classes.remove(file);

				try
				{
					fileLocks.get(file).close();
				}

				catch(IOException e1)
				{
					D.ww("Failed to properly unload classes from a failing-start module. This may cause issues when it is loaded again.");
				}

				fileLocks.remove(file);
				System.gc();
			}
		});
	}

	private ModuleDescription buildDescription(String pom) throws SAXException, IOException, ParserConfigurationException
	{
		ModuleDescription d = new ModuleDescription();

		for(String i : pom.split("\n"))
		{
			if(i.contains("=") && i.startsWith("version="))
			{
				d.setVersion(i.split("=")[1].trim());
			}

			if(i.contains("=") && i.startsWith("artifactId="))
			{
				d.setName(i.split("=")[1].trim());
			}
		}

		if(d.getVersion() != null && d.getName() != null)
		{
			return d;
		}

		return null;
	}

	private String findPom(String simpleName, JarScanner js)
	{
		for(String i : js.getPoms().k())
		{
			if(i.equals(simpleName))
			{
				return js.getPoms().get(i);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Module> findModuleClass(File file)
	{
		for(Class<?> i : classes.get(file))
		{
			if(Module.class.isAssignableFrom(i))
			{
				return (Class<? extends Module>) i;
			}
		}

		return null;
	}

	public void unloadModules()
	{
		for(File i : fileModules.k())
		{
			unloadModule(fileModules.get(i), () -> D.ll("Unloaded Module " + i.getName()));
		}

		try
		{
			PhantomModule.mod.executeModuleOperation(ModuleOperation.STOP);
			PhantomModule.mod.executeModuleOperation(ModuleOperation.UNREGISTER_ALL);
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	public GSet<Class<?>> getClasses(Module module)
	{
		return classes.get(module.getModuleFile());
	}

	public Module getModule(File i)
	{
		return fileModules.get(i);
	}

	public boolean hasModule(File i)
	{
		return fileModules.containsKey(i);
	}

	public GList<Module> getModules()
	{
		return fileModules.v().qadd(PhantomModule.mod);
	}
}

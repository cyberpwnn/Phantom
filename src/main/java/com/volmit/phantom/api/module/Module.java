package com.volmit.phantom.api.module;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.event.Listener;

import com.volmit.phantom.api.command.ICommand;
import com.volmit.phantom.api.command.PhantomPermission;
import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.registry.Registry;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.imp.module.AnnotationSeeker;
import com.volmit.phantom.imp.module.SeekableObject;
import com.volmit.phantom.main.Phantom;
import com.volmit.phantom.util.text.C;

public class Module extends SeekableObject implements IModule, Listener
{
	private D d;
	private File moduleFile;
	private ModuleDescription description;
	private AnnotationSeeker seekerStart;
	private AnnotationSeeker seekerStop;
	private AnnotationSeeker seekerTest;
	private AnnotationSeeker seekerInstance;
	private AnnotationSeeker seekerConfig;
	private AnnotationSeeker seekerCommand;
	private AnnotationSeeker seekerPermission;
	private Registry<Class<? extends IService>> serviceRegistry;
	private Registry<ICommand> commandRegistry;
	private Registry<PhantomPermission> permissionRegistry;

	public Module()
	{
		constructSeekers();
	}

	public Object executeModuleOperation(ModuleOperation op, Object... params)
	{
		switch(op)
		{
			case REGISTER_COMMANDS:
				return setEach(seekerCommand, (f) -> setCommand(f));
			case REGISTER_CONFIGS:
				return setEach(seekerConfig, (f) -> setConfig(f));
			case REGISTER_INSTANCES:
				return setAll(seekerInstance, Module.this);
			case REGISTER_PERMISSIONS:
				return setEach(seekerPermission, (f) -> setPermission(f));
			case START:
				return invokeAll(seekerStart);
			case STOP:
				return invokeAll(seekerStop);
			case TEST:
				return invokeSingle(seekerTest, (String) params[0], params[1]);
			case UNREGISTER_ALL:
				serviceRegistry.unregisterAll();
				commandRegistry.unregisterAll();
				permissionRegistry.unregisterAll();
			case REGISTER_SERVICES:
		}

		return null;
	}

	private Object setPermission(Field f)
	{
		try
		{
			Object o = f.getType().getConstructor().newInstance();
			// TODO set perm
			return o;
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private Object setCommand(Field f)
	{
		try
		{
			PhantomCommand o = (PhantomCommand) f.getType().getConstructor().newInstance();
			o.setCategory(f.getAnnotation(Command.class).value());
			return o;
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private Object setConfig(Field f)
	{
		try
		{
			Object o = f.getType().getConstructor().newInstance();
			String name = f.getAnnotation(Config.class).value();
			// TODO load/gen/stick/peel
			return o;
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private void constructSeekers()
	{
		seekerStart = new AnnotationSeeker(Start.class, (m) -> !Modifier.isStatic(m));
		seekerStop = new AnnotationSeeker(Stop.class, (m) -> !Modifier.isStatic(m));
		seekerTest = new AnnotationSeeker(Test.class, (m) -> Modifier.isStatic(m));
		seekerInstance = new AnnotationSeeker(Instance.class, (m) -> Modifier.isStatic(m) && !Modifier.isPrivate(m));
		seekerConfig = new AnnotationSeeker(Config.class, (m) -> Modifier.isStatic(m));
		seekerCommand = new AnnotationSeeker(Command.class, (m) -> !Modifier.isStatic(m));
		seekerPermission = new AnnotationSeeker(Command.class, (m) -> !Modifier.isStatic(m));
	}

	@Override
	public String getName()
	{
		return getDescription().getName();
	}

	@Override
	public String getVersion()
	{
		return getDescription().getVersion();
	}

	@Override
	public String getAuthors()
	{
		return getDescription().getAuthors();
	}

	@Override
	public C getColor()
	{
		return getDescription().getColor();
	}

	@Override
	public String getTag()
	{
		return C.DARK_GRAY + "[" + C.BOLD + getColor() + getName() + C.RESET + C.DARK_GRAY + "]" + C.GRAY + ": ";
	}

	@Override
	public String getTag(String sub)
	{
		return C.DARK_GRAY + "[" + C.BOLD + getColor() + getName() + C.RESET + C.DARK_GRAY + " - " + C.ITALIC + C.WHITE + sub + C.RESET + C.GRAY + "]" + C.GRAY + ": ";
	}

	@Override
	public File getModuleFile()
	{
		return moduleFile;
	}

	@Override
	public File getDataFile(String... path)
	{
		File ff = new File(getDataFolder(), new GList<String>(path).toString("/"));
		ff.getParentFile().mkdirs();
		return ff;
	}

	@Override
	public File getDataFolder(String... folders)
	{
		if(folders.length == 0)
		{
			return new File(Phantom.getModuleManager().getModuleFolder(), getName());
		}

		File ff = new File(getDataFolder(), new GList<String>(folders).toString("/"));
		ff.mkdirs();
		return ff;
	}

	public void l(Object... l)
	{
		if(d == null)
		{
			d = new D(getName());
		}

		d.l(l);
	}

	public void w(Object... l)
	{
		if(d == null)
		{
			d = new D(getName());
		}

		d.w(l);
	}

	public void f(Object... l)
	{
		if(d == null)
		{
			d = new D(getName());
		}

		d.f(l);
	}

	@Override
	public ModuleDescription getDescription()
	{
		return description;
	}

	public void configModified(String name)
	{
		// TODO Auto-generated method stub

	}
}
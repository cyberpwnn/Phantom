package com.volmit.phantom.api.module;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionDefault;

import com.volmit.phantom.api.command.PhantomPermission;
import com.volmit.phantom.api.config.Configurator;
import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.lang.GSet;
import com.volmit.phantom.api.lang.V;
import com.volmit.phantom.api.registry.ConfigRegistry;
import com.volmit.phantom.api.registry.ModuleRegistry;
import com.volmit.phantom.api.registry.Registry;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.imp.command.RouterCommand;
import com.volmit.phantom.imp.command.VirtualCommand;
import com.volmit.phantom.imp.module.AnnotationSeeker;
import com.volmit.phantom.imp.module.SeekableObject;
import com.volmit.phantom.main.Phantom;
import com.volmit.phantom.main.PhantomPlugin;
import com.volmit.phantom.util.text.C;

public class Module extends SeekableObject implements IModule, Listener, CommandExecutor
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
	private ConfigRegistry configRegistry;
	private GMap<GList<String>, VirtualCommand> commands;

	public Module()
	{
		constructRegistries();
		constructSeekers();
	}

	public Object executeModuleOperation(ModuleOperation op, Object... params)
	{
		try
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
					Object o = setEach(seekerPermission, (f) -> setPermission(f));
					registerAllPermissions();
					return o;
				case START:
					return invokeAll(seekerStart);
				case STOP:
					return invokeAll(seekerStop);
				case TEST:
					return invokeSingle(seekerTest, (String) params[0], params[1]);
				case UNREGISTER_ALL:
					unregisterCommands();
					unregisterPermissions();
					configRegistry.unregisterAll();
					serviceRegistry.unregisterAll();
				case REGISTER_SERVICES:
					registerServices();
			}
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public GSet<Class<?>> getModuleClasses()
	{
		return Phantom.getModuleManager().getClasses(this);
	}

	public GSet<Class<?>> getModuleClasses(Class<?> c)
	{
		GSet<Class<?>> cc = new GSet<Class<?>>();

		for(Class<?> i : getModuleClasses())
		{
			if(c.isAssignableFrom(i))
			{
				cc.add(i);
			}
		}

		return cc;
	}

	@SuppressWarnings("unchecked")
	private void registerServices()
	{
		for(Class<?> i : getModuleClasses(IService.class))
		{
			serviceRegistry.register((Class<? extends IService>) i);
		}
	}

	private void unregisterCommands()
	{
		for(Field i : seekerCommand.seekFields(getClass()))
		{
			try
			{
				unregisterCommand((PhantomCommand) i.get(this));
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	public void registerCommand(PhantomCommand cmd, String t)
	{
		commands.put(cmd.getAllNodes(), new VirtualCommand(this, cmd, t.trim().isEmpty() ? getTag() : getTag(t.trim())));
		PluginCommand cc = PhantomPlugin.plugin.getCommand(cmd.getNode().toLowerCase());

		if(cc != null)
		{
			cc.setExecutor(this);
			cc.setUsage(getName() + ":" + getClass().toString());
		}

		else
		{
			RouterCommand r = new RouterCommand(cmd, this);
			r.setUsage(getName() + ":" + getClass().toString());
			((CommandMap) new V(Bukkit.getServer()).get("commandMap")).register("", r);
		}
	}

	public void unregisterCommand(PhantomCommand cmd)
	{
		SimpleCommandMap m = new V(Bukkit.getServer()).get("commandMap");
		Map<String, Command> k = new V(m).get("knownCommands");

		for(Iterator<Map.Entry<String, Command>> it = k.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<String, Command> entry = it.next();
			if(entry.getValue() instanceof Command)
			{
				org.bukkit.command.Command c = (org.bukkit.command.Command) entry.getValue();
				String u = c.getUsage();

				if(u != null && u.equals(getName() + ":" + getClass().toString()))
				{
					D.as("Module Manager").l("Unregistering Command: " + c.getName());
					c.unregister(m);
					it.remove();
				}
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		GList<String> chain = new GList<String>().qadd(args);

		for(GList<String> i : commands.k())
		{
			for(String j : i)
			{
				if(j.equalsIgnoreCase(label))
				{
					VirtualCommand cmd = commands.get(i);

					if(cmd.hit(sender, chain.copy()))
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	private Object setPermission(Field f)
	{
		try
		{
			PhantomPermission o = (PhantomPermission) f.getType().getConstructor().newInstance();

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
			registerCommand(o, getTag(f.getAnnotation(Command.class).value()));
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
			File file = getDataFile(name.replaceAll(".yml", "").toLowerCase() + ".yml");
			if(Configurator.load(o, file))
			{
				configRegistry.register(file, () -> Configurator.load(o, file));
			}

			else
			{
				f("Failed to load configuration: " + file.getName());
			}

			return o;
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private void constructRegistries()
	{
		commands = new GMap<>();
		configRegistry = new ConfigRegistry();
		serviceRegistry = new ModuleRegistry<Class<? extends IService>>();
	}

	private void constructSeekers()
	{
		seekerStart = new AnnotationSeeker(Start.class, (m) -> !Modifier.isStatic(m));
		seekerStop = new AnnotationSeeker(Stop.class, (m) -> !Modifier.isStatic(m));
		seekerTest = new AnnotationSeeker(Test.class, (m) -> Modifier.isStatic(m));
		seekerInstance = new AnnotationSeeker(Instance.class, (m) -> Modifier.isStatic(m) && !Modifier.isPrivate(m));
		seekerConfig = new AnnotationSeeker(Config.class, (m) -> Modifier.isStatic(m));
		seekerCommand = new AnnotationSeeker(Command.class, (m) -> !Modifier.isStatic(m));
		seekerPermission = new AnnotationSeeker(Permission.class, (m) -> Modifier.isPublic(m));
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

	private void registerAllPermissions()
	{
		for(org.bukkit.permissions.Permission i : computePermissions())
		{
			try
			{
				Bukkit.getPluginManager().addPermission(i);
			}

			catch(Throwable e)
			{

			}
		}
	}

	private void unregisterPermissions()
	{
		for(org.bukkit.permissions.Permission i : computePermissions())
		{
			Bukkit.getPluginManager().removePermission(i);
		}
	}

	private GList<org.bukkit.permissions.Permission> computePermissions()
	{
		GList<org.bukkit.permissions.Permission> g = new GList<>();
		for(Field i : seekerPermission.seekFields(getClass()))
		{
			try
			{
				PhantomPermission x = (PhantomPermission) i.get(Modifier.isStatic(i.getModifiers()) ? null : this);
				g.add(toPermission(x));
				g.addAll(computePermissions(x));
			}

			catch(IllegalArgumentException | IllegalAccessException | SecurityException e)
			{
				e.printStackTrace();
			}
		}

		return g.removeDuplicates();
	}

	private GList<org.bukkit.permissions.Permission> computePermissions(PhantomPermission p)
	{
		GList<org.bukkit.permissions.Permission> g = new GList<>();

		for(PhantomPermission i : p.getChildren())
		{
			g.add(toPermission(i));
			g.addAll(computePermissions(i));
		}

		return g;
	}

	private org.bukkit.permissions.Permission toPermission(PhantomPermission p)
	{
		org.bukkit.permissions.Permission perm = new org.bukkit.permissions.Permission(p.getFullNode() + (p.hasParent() ? "" : ".*"));
		perm.setDescription(p.getDescription() == null ? "" : p.getDescription());
		perm.setDefault(p.isDefault() ? PermissionDefault.TRUE : PermissionDefault.OP);

		for(PhantomPermission i : p.getChildren())
		{
			perm.getChildren().put(i.getFullNode(), true);
		}

		return perm;
	}
}

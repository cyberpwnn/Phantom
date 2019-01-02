package com.volmit.phantom.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissionDefault;

import com.volmit.phantom.json.JSONArray;
import com.volmit.phantom.json.JSONObject;
import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.F;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.Profiler;
import com.volmit.phantom.lang.V;
import com.volmit.phantom.lang.VIO;
import com.volmit.phantom.plugin.Actionable.ActionType;
import com.volmit.phantom.plugin.Scaffold.Async;
import com.volmit.phantom.plugin.Scaffold.Command;
import com.volmit.phantom.plugin.Scaffold.Config;
import com.volmit.phantom.plugin.Scaffold.ConsoleTest;
import com.volmit.phantom.plugin.Scaffold.Instance;
import com.volmit.phantom.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.plugin.Scaffold.Permission;
import com.volmit.phantom.plugin.Scaffold.PlayerTest;
import com.volmit.phantom.plugin.Scaffold.Start;
import com.volmit.phantom.plugin.Scaffold.Stop;
import com.volmit.phantom.plugin.Scaffold.Test;
import com.volmit.phantom.plugin.Scaffold.Tick;
import com.volmit.phantom.services.ConfigSVC;
import com.volmit.phantom.text.C;

public class StructuredModule implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Module module;
	private File fd;
	private GMap<String, Object> configurations;
	private GList<Actionable> actions;
	private GList<PhantomCommand> commands;
	private GList<PhantomPermission> permissions;
	private ModuleInfo moduleInfo;
	private D d;

	public StructuredModule(Module module, File fd) throws AbstractModuleException
	{
		this.module = module;
		commands = new GList<>();
		permissions = new GList<>();
		configurations = new GMap<>();
		actions = new GList<>();
		this.fd = fd;
		int t = 0;

		try
		{
			moduleInfo = module.getClass().getAnnotation(ModuleInfo.class);
			d = new D("ModuleManager > " + moduleInfo.name());
			Class<?> q = module.getClass();

			for(Field i : q.getDeclaredFields())
			{
				i.setAccessible(true);
				boolean priv = Modifier.isPrivate(i.getModifiers());
				boolean stat = Modifier.isStatic(i.getModifiers());

				if(i.isAnnotationPresent(Instance.class))
				{
					if(priv)
					{
						d.w("What good is an instance field (" + i.getName() + ") if its private?");
					}

					if(!stat)
					{
						i.set(module, module);
						d.w("What good is an instance field (" + i.getName() + ") if its not static?");
					}

					else
					{
						i.set(null, module);
					}
				}

				else if(i.isAnnotationPresent(Permission.class))
				{
					PhantomPermission px = (PhantomPermission) i.getType().getConstructor().newInstance();
					permissions.add(px);
					i.set(stat ? null : module, px);
				}

				else if(i.isAnnotationPresent(Config.class))
				{
					if(!stat)
					{
						d.w("Cannot set a non static config refrence: " + i.getName());
						continue;
					}

					Config c = i.getAnnotation(Config.class);
					Object config = i.getType().getConstructor().newInstance();
					configurations.put(c.value(), config);
				}

				else if(i.isAnnotationPresent(Command.class))
				{
					if(stat)
					{
						d.w("Cannot set a static command refrence: " + i.getName());
						continue;
					}

					PhantomCommand v = (PhantomCommand) i.getType().getConstructor().newInstance();
					commands.add(v);
					module.registerCommand(v, i.getAnnotation(Command.class).value());
				}
			}

			for(Method i : q.getDeclaredMethods())
			{
				i.setAccessible(true);
				boolean stat = Modifier.isStatic(i.getModifiers());

				if(i.isAnnotationPresent(Start.class))
				{
					if(stat)
					{
						d.w("Start method " + i.getName() + "() is static. Ignoring.");
						continue;
					}

					actions.add(new ChronoAction(i, ActionType.START, i.isAnnotationPresent(Async.class)));
				}

				else if(i.isAnnotationPresent(Stop.class))
				{
					if(stat)
					{
						d.w("Stop method " + i.getName() + "() is static. Ignoring.");
						continue;
					}

					actions.add(new ChronoAction(i, ActionType.STOP, i.isAnnotationPresent(Async.class)));
				}

				else if(i.isAnnotationPresent(Tick.class))
				{
					if(stat)
					{
						d.w("Tick method " + i.getName() + "() is static. Ignoring.");
						continue;
					}

					Tick tx = i.getAnnotation(Tick.class);

					if(tx.value() < 0)
					{
						d.w("Tick method " + i.getName() + "() Interval is less than 0. Ignoring.");
						continue;
					}

					actions.add(new TaskAction(i, ActionType.TICK, i.isAnnotationPresent(Async.class), tx.value()));
				}

				else if(i.isAnnotationPresent(Test.class) && i.getParameterCount() == 1 && i.getParameterTypes()[0].equals(PhantomSender.class))
				{
					if(stat)
					{
						d.w("Test method " + i.getName() + "() is static. Ignoring.");
						continue;
					}

					t++;
					actions.add(new TestAction(i, ActionType.TEST, i.isAnnotationPresent(Async.class), TestType.ANY));
				}

				else if(i.isAnnotationPresent(PlayerTest.class) && i.getParameterCount() == 1 && i.getParameterTypes()[0].equals(Player.class))
				{
					if(stat)
					{
						d.w("Test method " + i.getName() + "() is static. Ignoring.");
						continue;
					}

					t++;
					actions.add(new TestAction(i, ActionType.TEST, i.isAnnotationPresent(Async.class), TestType.PLAYER));
				}

				else if(i.isAnnotationPresent(ConsoleTest.class) && i.getParameterCount() == 1 && i.getParameterTypes()[0].equals(PhantomSender.class))
				{
					if(stat)
					{
						d.w("Test method " + i.getName() + "() is static. Ignoring.");
						continue;
					}

					t++;
					actions.add(new TestAction(i, ActionType.TEST, i.isAnnotationPresent(Async.class), TestType.CONSOLE));
				}
			}

			loadConfigs();
			registerAllPermissions();
			d.l("Permissions: " + permissions.size() + " -> " + computePermissions().size() + " Nodes");
			d.l("Structured " + C.RESET + moduleInfo.color() + moduleInfo.name() + C.WHITE + " " + moduleInfo.version() + " by " + moduleInfo.author() + ". Tests: " + t);
		}

		catch(Throwable e)
		{
			throw new AbstractModuleException(e.getMessage(), e);
		}

		new S(200)
		{
			@Override
			public void run()
			{
				try
				{
					new File(Phantom.getModuleManager().getDataFolder(), getInfo().name()).mkdirs();
					VIO.writeAll(new File(new File(Phantom.getModuleManager().getDataFolder(), getInfo().name()), "structure.json"), computeMetadata().toString(2));
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		};
	}

	private void loadConfigs()
	{
		for(String i : configurations.k())
		{
			try
			{
				loadConfig(i);
				SVC.get(ConfigSVC.class).registerConfigForHotload(module.getDataFile(i + ".yml"), module);
			}

			catch(IOException | InvalidConfigurationException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void configModified(String name)
	{
		try
		{
			loadConfig(name.replaceAll(".yml", ""));
		}

		catch(IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	private void loadConfig(String c) throws IOException, InvalidConfigurationException
	{
		File f = module.getDataFile(c + ".yml");
		f.getParentFile().mkdirs();
		FileConfiguration fc = peelConfig(c);

		if(f.exists())
		{
			fc.load(f);
		}

		stickConfig(c, fc);
		saveConfig(c);
	}

	private void saveConfig(String c) throws IOException
	{
		File f = module.getDataFile(c + ".yml");
		f.getParentFile().mkdirs();
		peelConfig(c).save(f);
	}

	private FileConfiguration peelConfig(String s)
	{
		FileConfiguration fc = new YamlConfiguration();

		for(Field i : configurations.get(s).getClass().getDeclaredFields())
		{
			if(Modifier.isStatic(i.getModifiers()) && !Modifier.isFinal(i.getModifiers()))
			{
				fc.set(i.getName().toLowerCase().replaceAll("_", "."), new V(configurations.get(s)).get(i.getName()));
			}
		}

		return fc;
	}

	private void stickConfig(String s, FileConfiguration fc)
	{
		for(Field i : configurations.get(s).getClass().getDeclaredFields())
		{
			String sv = i.getName().toLowerCase().replaceAll("_", ".");

			if(fc.contains(sv))
			{
				try
				{
					new V(configurations.get(s)).set(i.getName(), fc.get(sv));
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	protected JSONObject computeMetadata()
	{
		JSONObject o = new JSONObject();
		o.put("properties", computeMetadataProperties());
		o.put("registries", computeMetadataRegistries());

		return o;
	}

	private JSONObject computeMetadataRegistries()
	{
		JSONObject o = new JSONObject();
		o.put("commands", computeMetadataCommands());
		o.put("permissions", computeMetadataPermissions());
		o.put("services", computeMetadataServices());
		o.put("tests", computeMetadataTests());
		o.put("configs", computeMetadataConfigs());

		return o;
	}

	private JSONArray computeMetadataConfigs()
	{
		JSONArray o = new JSONArray();

		for(String i : configurations.k())
		{
			JSONObject v = new JSONObject();
			v.put("path", "modules/" + getInfo().name() + "/" + i + ".yml");
			v.put("anchor", configurations.get(i).getClass().getCanonicalName());
			o.put(v);
		}

		return o;
	}

	private JSONArray computeMetadataCommands()
	{
		JSONArray o = new JSONArray();

		for(PhantomCommand i : commands)
		{
			o.put(computeMetadataCommands(i));
		}

		return o;
	}

	private JSONObject computeMetadataCommands(PhantomCommand c)
	{
		JSONObject o = new JSONObject();
		o.put("node", c.getNode());
		o.put("nodes", c.getAllNodes().toJSONStringArray());
		JSONArray ja = new JSONArray();

		for(PhantomCommand i : c.getChildren())
		{
			ja.put(computeMetadataCommands(i));
		}

		o.put("children", ja);
		o.put("required-permissions", c.getRequiredPermissions().toJSONStringArray());

		return o;
	}

	private JSONArray computeMetadataPermissions()
	{
		JSONArray o = new JSONArray();

		for(org.bukkit.permissions.Permission i : computePermissions())
		{
			o.put(computeMetadataPermission(i));
		}

		return o;
	}

	private JSONObject computeMetadataPermission(org.bukkit.permissions.Permission i)
	{
		JSONObject o = new JSONObject();
		o.put("node", i.getName());
		o.put("description", i.getDescription());
		o.put("default", i.getDefault().name());
		o.put("children", new GList<String>(i.getChildren().keySet()).toJSONStringArray());

		return o;
	}

	private JSONArray computeMetadataTests()
	{
		JSONArray o = new JSONArray();

		for(Actionable i : actions)
		{
			if(i.getType().equals(ActionType.TEST))
			{
				JSONObject j = new JSONObject();
				TestAction a = (TestAction) i;
				j.put("name", a.getMethod().getName() + "()");
				j.put("type", a.type.name());
				j.put("async", a.isAsync());
				o.put(j);
			}
		}

		return o;
	}

	private JSONArray computeMetadataServices()
	{
		JSONArray o = new JSONArray();

		try
		{
			for(Class<? extends IService> i : Phantom.getRunningServices())
			{
				File fx = new File(i.getProtectionDomain().getCodeSource().getLocation().getFile());

				if(getModuleFile() == null || (getModuleFile() != null && fx.equals(module.getModuleFile())))
				{
					o.put(i.toString());
				}
			}
		}

		catch(Throwable e)
		{

		}

		return o;
	}

	private JSONObject computeMetadataProperties()
	{
		JSONObject o = new JSONObject();
		o.put("name", getInfo().name());
		o.put("version", getInfo().version());
		o.put("author", getInfo().author());
		o.put("color", getInfo().color().name());

		return o;
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

	protected void exportPermissions()
	{

	}

	protected void unregisterAllPermissions()
	{
		for(org.bukkit.permissions.Permission i : computePermissions())
		{
			Bukkit.getPluginManager().removePermission(i);
		}
	}

	private GList<org.bukkit.permissions.Permission> computePermissions()
	{
		GList<org.bukkit.permissions.Permission> g = new GList<>();
		for(Field i : module.getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Permission.class))
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

	public Module getModule()
	{
		return module;
	}

	public File getFd()
	{
		return fd;
	}

	public GList<Actionable> getActions()
	{
		return actions;
	}

	public GList<PhantomCommand> getCommands()
	{
		return commands;
	}

	public ModuleInfo getModuleInfo()
	{
		return moduleInfo;
	}

	public D getD()
	{
		return d;
	}

	public ModuleInfo getInfo()
	{
		return moduleInfo;
	}

	public D getDispatcher()
	{
		return d;
	}

	public void start()
	{
		for(Actionable i : actions)
		{
			if(i instanceof ChronoAction && i.getType().equals(ActionType.START))
			{
				ChronoAction c = (ChronoAction) i;

				if(c.isAsync())
				{
					Phantom.afterStartup(() -> new R().async(() -> i.invoke(module)).start());
				}

				else
				{
					i.invoke(module);
				}
			}

			if(i instanceof TaskAction && i.getType().equals(ActionType.TICK))
			{
				TaskAction ta = (TaskAction) i;

				Phantom.afterStartup(new Runnable()
				{
					@Override
					public void run()
					{
						if(ta.isAsync())
						{
							ta.ar = new AR(ta.getInterval())
							{
								@Override
								public void run()
								{
									ta.invoke(module);
								}
							};
						}

						else
						{
							ta.sr = new SR(ta.getInterval())
							{
								@Override
								public void run()
								{
									ta.invoke(module);
								}
							};
						}
					}
				});

			}
		}

		Phantom.afterStartup(() -> Bukkit.getPluginManager().registerEvents(module, PhantomPlugin.plugin));
	}

	public GList<String> getTests(PhantomSender sender)
	{
		GList<String> s = new GList<>();

		for(Actionable i : actions)
		{
			if(i instanceof TestAction && i.getType().equals(ActionType.TEST))
			{
				TestAction c = (TestAction) i;
				if(c.type.equals(TestType.ANY) || (sender.isPlayer() && c.type.equals(TestType.PLAYER) || (!sender.isPlayer() && c.type.equals(TestType.CONSOLE))))
				{
					s.add(c.method.getName() + "()" + (c.isAsync() ? C.WHITE + " [async]" : ""));
				}
			}
		}

		return s;
	}

	public GList<String> getTestsRaw(PhantomSender sender)
	{
		GList<String> s = new GList<>();

		for(Actionable i : actions)
		{
			if(i instanceof TestAction && i.getType().equals(ActionType.TEST))
			{
				TestAction c = (TestAction) i;
				if(c.type.equals(TestType.ANY) || (sender.isPlayer() && c.type.equals(TestType.PLAYER) || (!sender.isPlayer() && c.type.equals(TestType.CONSOLE))))
				{
					s.add(c.method.getName());
				}
			}
		}

		return s;
	}

	public void test(PhantomSender sender, String test)
	{
		for(Actionable i : actions)
		{
			if(i instanceof TestAction && i.getType().equals(ActionType.TEST))
			{
				TestAction c = (TestAction) i;
				if(!(c.method.getName()).equalsIgnoreCase(test))
				{
					continue;
				}

				boolean fail = false;
				Object o = sender;
				switch(c.type)
				{
					case ANY:
						sender.sendMessage("Testing " + C.WHITE + test + "()");
						break;
					case CONSOLE:
						if(sender.isPlayer())
						{
							sender.sendMessage("Only consoles can test " + C.RED + test + "()");
							fail = true;
						}

						else
						{
							sender.sendMessage("Testing " + C.WHITE + test + "()");
						}

						break;
					case PLAYER:
						if(sender.isPlayer())
						{
							sender.sendMessage("Testing " + C.WHITE + test + "()");
							o = sender.player();
						}

						else
						{
							sender.sendMessage("Only players can test " + C.RED + test + "()");
							fail = true;
						}

						break;
					default:
						break;
				}

				if(fail)
				{
					return;
				}

				if(c.isAsync())
				{
					Object m = o;
					Profiler px = new Profiler();
					px.begin();
					new R().async(() -> c.invoke(module, m)).then().sync(() -> px.end()).then().sync(() -> sender.sendMessage("Test ran async:" + C.WHITE + test + "(" + F.time(px.getMilliseconds(), 1) + ")")).start();
				}

				else
				{
					Profiler px = new Profiler();
					px.begin();
					c.invoke(module, o);
					px.end();
					sender.sendMessage("Test ran " + C.WHITE + test + "(" + F.time(px.getMilliseconds(), 1) + ")");
				}
			}
		}
	}

	public void stop()
	{
		if(module == null)
		{
			d.w("Module is null!");
		}

		HandlerList.unregisterAll(module);
		SVC.get(ConfigSVC.class).unregisterConfigs(module);

		for(Actionable i : actions)
		{
			if(i instanceof ChronoAction && i.getType().equals(ActionType.STOP))
			{
				ChronoAction c = (ChronoAction) i;

				if(c.isAsync())
				{
					Phantom.afterStartup(() -> new R().async(() -> i.invoke(module)).start());
				}

				else
				{
					i.invoke(module);
				}
			}

			if(i instanceof TaskAction && i.getType().equals(ActionType.TICK))
			{
				TaskAction ta = (TaskAction) i;

				try
				{
					if(ta.isAsync())
					{
						ta.ar.cancel();
					}

					else
					{
						ta.sr.cancel();
					}
				}

				catch(Throwable e)
				{

				}
			}
		}
	}

	public File getModuleFile()
	{
		return fd;
	}
}
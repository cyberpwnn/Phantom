package com.volmit.phantom.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.F;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.Profiler;
import com.volmit.phantom.plugin.Actionable.ActionType;
import com.volmit.phantom.plugin.Scaffold.Async;
import com.volmit.phantom.plugin.Scaffold.Command;
import com.volmit.phantom.plugin.Scaffold.ConsoleTest;
import com.volmit.phantom.plugin.Scaffold.Instance;
import com.volmit.phantom.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.plugin.Scaffold.PlayerTest;
import com.volmit.phantom.plugin.Scaffold.Start;
import com.volmit.phantom.plugin.Scaffold.Stop;
import com.volmit.phantom.plugin.Scaffold.Test;
import com.volmit.phantom.plugin.Scaffold.Tick;
import com.volmit.phantom.text.C;

public class StructuredModule
{
	private Module module;
	private GList<Actionable> actions;
	private ModuleInfo moduleInfo;
	private D d;

	public ModuleInfo getInfo()
	{
		return moduleInfo;
	}

	public D getDispatcher()
	{
		return d;
	}

	public StructuredModule(Module module) throws AbstractModuleException
	{
		this.module = module;
		actions = new GList<>();
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

				else if(i.isAnnotationPresent(Command.class))
				{
					if(stat)
					{
						d.w("Cannot set a static command refrence: " + i.getName());
						continue;
					}

					module.registerCommand((PhantomCommand) i.getType().getConstructor().newInstance(), i.getAnnotation(Command.class).value());
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

			d.l("Structured " + C.RESET + moduleInfo.color() + moduleInfo.name() + C.WHITE + " " + moduleInfo.version() + " by " + moduleInfo.author() + ". Tests: " + t);
		}

		catch(Throwable e)
		{
			throw new AbstractModuleException(e.getMessage(), e);
		}
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
		HandlerList.unregisterAll(module);

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
}
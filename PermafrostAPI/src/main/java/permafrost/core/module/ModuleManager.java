package permafrost.core.module;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import permafrost.core.lang.GList;
import permafrost.core.lang.GMap;

public class ModuleManager
{
	private final GMap<Module, Boolean> modules;
	private final GMap<Module, Integer> timings;
	private final GList<Module> order;
	private final Plugin pl;
	private final int[] task;
	
	public ModuleManager(Plugin pl)
	{
		this.pl = pl;
		modules = new GMap<Module, Boolean>();
		timings = new GMap<Module, Integer>();
		order = new GList<Module>();
		task = new int[]{-1};
	}
	
	public void register(Module module)
	{
		modules.put(module, false);
	}
	
	public void registerListener(Listener listener)
	{
		pl.getServer().getPluginManager().registerEvents(listener, pl);
	}
	
	public boolean enabled(Module module)
	{
		return modules.containsKey(module) && modules.get(module);
	}
	
	public Class<? extends Module> getModuleClass(Module module)
	{
		return module.getClass();
	}
	
	public Module getModule(Class<? extends Module> module)
	{
		for(Module i : modules.k())
		{
			if(getModuleClass(i).equals(module))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Integer getTiming(Module module)
	{
		if(module.getInterval() == null)
		{
			return null;
		}
		
		return module.getInterval().value();
	}
	
	public void disable()
	{
		for(Module i : order)
		{
			i.onDisable();
		}
		
		pl.getServer().getScheduler().cancelTask(task[0]);
	}
	
	public void enable()
	{
		for(Module i : modules.k())
		{
			i.onEnable();
		}
		
		task[0] = pl.getServer().getScheduler().scheduleSyncRepeatingTask(pl, new Runnable()
		{
			public void run()
			{
				for(Module i : timings.k())
				{
					timings.put(i, timings.get(i) - 1);
					
					if(timings.get(i) <= 0)
					{
						timings.put(i, getTiming(i));
						i.onTick();
					}
				}
			}
		}, 0, 0);
	}
	
	public void enable(Module module) throws UnknownModuleDependencyException
	{
		if(enabled(module))
		{
			return;
		}
		
		for(Class<? extends Module> i : module.getDependencies())
		{
			Module m = getModule(i);
			
			if(m != null)
			{
				enable(m);
			}
			
			else
			{
				throw new UnknownModuleDependencyException(module, i);
			}
		}
		
		module.onEnable();
		order.add(module);
		modules.put(module, true);
		
		if(getTiming(module) != null)
		{
			timings.put(module, getTiming(module));
		}
	}
	
	public Plugin getPlugin()
	{
		return pl;
	}

	public GMap<Module, Boolean> getModules()
	{
		return modules.copy();
	}
}

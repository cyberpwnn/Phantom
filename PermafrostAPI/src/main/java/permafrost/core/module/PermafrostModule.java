package permafrost.core.module;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import permafrost.core.lang.GList;

public class PermafrostModule implements Module, Listener
{
	private final String name;
	private final Plugin pl;
	private final ModuleManager manager;
	
	public PermafrostModule(ModuleManager manager)
	{
		this.pl = manager.getPlugin();
		this.name = this.getClass().getSimpleName();
		this.manager = manager;
		this.manager.register(this);
		this.manager.registerListener(this);
	}
	
	public void onEnable()
	{
		
	}
	
	public void onDisable()
	{
		
	}
	
	public void onTick()
	{
		
	}

	public String getName()
	{
		return name;
	}

	public Plugin getPlugin()
	{
		return pl;
	}
	
	public Interval getInterval()
	{
		return getClass().getDeclaredAnnotation(Interval.class);
	}
	
	public GList<Class<? extends Module>> getDependencies()
	{
		GList<Class<? extends Module>> modules = new GList<Class<? extends Module>>();
		Depend depend = getClass().getDeclaredAnnotation(Depend.class);
		
		if(depend != null)
		{
			for(Class<? extends Module> i : depend.value())
			{
				modules.add(i);
			}
		}
		
		return modules;
	}
	
	public ModuleManager getManager()
	{
		return manager;
	}
}

package org.cyberpwn.phantom.construct;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.ConfigurationHandler;
import org.cyberpwn.phantom.command.CommandListener;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;
import org.cyberpwn.phantom.sync.Task;
import org.cyberpwn.phantom.util.Average;
import org.cyberpwn.phantom.util.D;
import org.cyberpwn.phantom.util.DMSRequire;
import org.cyberpwn.phantom.util.DMSRequirement;
import org.cyberpwn.phantom.util.F;
import org.cyberpwn.phantom.util.Timer;

/**
 * A controllable plugin which can act as a plugin and a controller
 * 
 * @author cyberpwn
 */
public class ControllablePlugin extends JavaPlugin implements Controllable
{
	protected GList<Controllable> controllers;
	protected GMap<Controllable, Integer> timings;
	protected GMap<Controllable, Integer> liveTimings;
	protected D d;
	protected Task task;
	private Average time;
	
	public void enable()
	{
		
	}
	
	public void disable()
	{
		
	}
	
	@Override
	public void onEnable()
	{
		controllers = new GList<Controllable>();
		timings = new GMap<Controllable, Integer>();
		liveTimings = new GMap<Controllable, Integer>();
		time = new Average(12);
		d = new D(getName());
		
		if(getClass().isAnnotationPresent(DMSRequire.class))
		{
			DMSRequire dms = getClass().getAnnotation(DMSRequire.class);
			
			if(dms.value().equals(DMSRequirement.SQL))
			{
				Phantom.instance().getDms().needsSQL(this);
			}
		}
		
		enable();
		start();
		
		registerTicked(this);
		Phantom.instance().registerPlugin(this);
		Phantom.instance().bindController(this);
		
		d.s("Started");
	}
	
	public GList<Controllable> getAllControllers()
	{
		return getAllControllers(this);
	}
	
	private GList<Controllable> getAllControllers(Controllable cx)
	{
		GList<Controllable> c = new GList<Controllable>();
		
		for(Controllable i : cx.getControllers())
		{
			if(!i.getControllers().isEmpty())
			{
				c.add(getAllControllers(i));
			}
			
			c.add(i);
		}
		
		return c;
	}
	
	/**
	 * Load data from a mysql database. If it doesnt exists, nothing will be
	 * added to the cluster, and nothing will be created in the database
	 * Requires the Tabled annotation
	 * 
	 * @param c
	 *            the configurable object
	 * @param finish
	 *            the onFinish
	 */
	public void loadMysql(Configurable c, Runnable finish)
	{
		if(!ConfigurationHandler.hasTable(c))
		{
			d.f("No Tabled annotation for the configurable object " + c.getClass().getSimpleName() + "<" + c.getCodeName() + ">");
			return;
		}
		
		Phantom.instance().loadSql(c, finish);
	}
	
	/**
	 * Load data from a mysql database. If it doesnt exists, nothing will be
	 * added to the cluster, and nothing will be created in the database
	 * Requires the Tabled annotation
	 * 
	 * @param c
	 *            the configurable object
	 */
	public void loadMysql(Configurable c)
	{
		if(!ConfigurationHandler.hasTable(c))
		{
			d.f("No Tabled annotation for the configurable object " + c.getClass().getSimpleName() + "<" + c.getCodeName() + ">");
			return;
		}
		
		Phantom.instance().loadSql(c, new Runnable()
		{
			@Override
			public void run()
			{
				
			}
		});
	}
	
	/**
	 * Saves data to a mysql database. Requires the Tabled annotation
	 * 
	 * @param c
	 *            the configurable object
	 * @param connection
	 *            the database connection data
	 */
	public void saveMysql(Configurable c)
	{
		if(!ConfigurationHandler.hasTable(c))
		{
			d.f("No Tabled annotation for the configurable object " + c.getClass().getSimpleName() + "<" + c.getCodeName() + ">");
			return;
		}
		
		Phantom.instance().saveSql(c, new Runnable()
		{
			@Override
			public void run()
			{
				
			}
		});
	}
	
	/**
	 * Saves data to a mysql database. Requires the Tabled annotation
	 * 
	 * @param c
	 *            the configurable object
	 * @param connection
	 *            the database connection data
	 * @param finish
	 *            called when the data was saved
	 */
	public void saveMysql(Configurable c, Runnable finish)
	{
		if(!ConfigurationHandler.hasTable(c))
		{
			d.f("No Tabled annotation for the configurable object " + c.getClass().getSimpleName() + "<" + c.getCodeName() + ">");
			return;
		}
		
		Phantom.instance().saveSql(c, finish);
	}
	
	public void registerTicked(Controllable c)
	{
		Ticked t = c.getClass().getAnnotation(Ticked.class);
		
		if(t != null)
		{
			int v = t.value();
			
			if(v <= 0)
			{
				v = 1;
			}
			
			timings.put(c, v);
			liveTimings.put(c, v);
			d.s("Tickable: " + ChatColor.LIGHT_PURPLE + c.toString() + ChatColor.YELLOW + " @" + F.f((20.0 / (double) v), 2) + " tps");
		}
		
		for(Controllable i : c.getControllers())
		{
			registerTicked(i);
		}
	}
	
	@Override
	public void onDisable()
	{
		disable();
		stop();
		d.s(ChatColor.RED + "Stopped");
	}
	
	@Override
	public void start()
	{
		for(Controllable i : controllers)
		{
			i.start();
		}
		
		onStart();
		
		task = new Task(this, 0)
		{
			public void run()
			{
				Timer t = new Timer();
				t.start();
				
				for(Controllable i : liveTimings.k())
				{
					liveTimings.put(i, liveTimings.get(i) - 1);
					
					if(liveTimings.get(i) <= 0)
					{
						i.tick();
						liveTimings.put(i, timings.get(i));
					}
				}
				
				t.stop();
				time.put(t.getTime());
			}
		};
	}
	
	@Override
	public void stop()
	{
		try
		{
			task.cancel();
			
			for(Controllable i : controllers)
			{
				try
				{
					i.stop();
				}
				
				catch(Exception e)
				{
					
				}
			}
			
			onStop();
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public void tick()
	{
		onTick();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public void onTick()
	{
		
	}
	
	@Override
	public GList<Controllable> getControllers()
	{
		return controllers;
	}
	
	@Override
	public ControllablePlugin getPlugin()
	{
		return this;
	}
	
	@Override
	public Controllable getParentController()
	{
		return null;
	}
	
	@Override
	public void unregister(Controllable c)
	{
		controllers.remove(c);
		
		try
		{
			Phantom.instance().unbindController(c);
		}
		
		catch(Exception e)
		{
			
		}
		
		if(c instanceof CommandListener)
		{
			Phantom.instance().getCommandRegistryController().unregister((CommandListener) c);
		}
	}
	
	@Override
	public void register(Controller c)
	{
		controllers.add(c);
		
		try
		{
			Phantom.instance().bindController(c);
		}
		
		catch(Exception e)
		{
			
		}
		
		if(c instanceof CommandListener)
		{
			Phantom.instance().getCommandRegistryController().register((CommandListener) c);
		}
	}
	
	public void registerListener(Listener listener)
	{
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	public void unRegisterListener(Listener listener)
	{
		HandlerList.unregisterAll(listener);
	}
	
	public int scheduleSyncRepeatingTask(int delay, int interval, Runnable runnable)
	{
		return getServer().getScheduler().scheduleSyncRepeatingTask(this, runnable, delay, interval);
	}
	
	public int scheduleSyncTask(int delay, Runnable runnable)
	{
		return getServer().getScheduler().scheduleSyncDelayedTask(this, runnable, delay);
	}
	
	public void cancelTask(int tid)
	{
		getServer().getScheduler().cancelTask(tid);
	}
	
	@Override
	public double getTime()
	{
		return time.getAverage();
	}
	
	@Override
	public void reload()
	{
		onReload();
		stop();
		start();
	}
	
	@Override
	public void onReload()
	{
		
	}
	
}

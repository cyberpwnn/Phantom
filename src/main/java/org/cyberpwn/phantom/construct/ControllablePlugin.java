package org.cyberpwn.phantom.construct;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;
import org.cyberpwn.phantom.sync.Task;
import org.cyberpwn.phantom.util.D;
import org.cyberpwn.phantom.util.F;

import org.bukkit.ChatColor;

/**
 * A controllable plugin which can act as a plugin and a controller
 * 
 * @author cyberpwn
 *
 */
public class ControllablePlugin extends JavaPlugin implements Controllable
{
	protected GList<Controllable> controllers;
	protected GMap<Controllable, Integer> timings;
	protected GMap<Controllable, Integer> liveTimings;
	protected D d;
	protected Task task;
	
	public void enable()
	{
		
	}
	
	public void disable()
	{
		
	}
	
	@Override
	public void onEnable()
	{
		d = new D(getName());
		controllers = new GList<Controllable>();
		timings = new GMap<Controllable, Integer>();
		liveTimings = new GMap<Controllable, Integer>();
		enable();
		start();
		
		registerTicked(this);
		
		d.s("Started");
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
				for(Controllable i : liveTimings.k())
				{
					liveTimings.put(i, liveTimings.get(i) - 1);
					
					if(liveTimings.get(i) <= 0)
					{
						i.tick();
						liveTimings.put(i, timings.get(i));
					}
				}
			}
		};
	}
	
	@Override
	public void stop()
	{
		task.cancel();
		
		for(Controllable i : controllers)
		{
			i.stop();
		}
		
		onStop();
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
	public void register(Controller c)
	{
		controllers.add(c);
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
}

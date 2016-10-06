package org.phantomapi.construct;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurationHandler;
import org.phantomapi.command.CommandListener;
import org.phantomapi.core.SyncStart;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.network.Network;
import org.phantomapi.sync.S;
import org.phantomapi.sync.Task;
import org.phantomapi.util.Average;
import org.phantomapi.util.C;
import org.phantomapi.util.D;
import org.phantomapi.util.DMSRequire;
import org.phantomapi.util.DMSRequirement;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.util.T;
import org.phantomapi.util.Timer;

public abstract class ControllableEnvironment extends WrappedJavaPlugin implements Controllable
{
	protected GList<Controllable> controllers;
	protected GMap<Controllable, Integer> timings;
	protected GMap<Controllable, Integer> liveTimings;
	protected D d;
	protected Task task;
	private Average time;
	private String named;
	
	public ControllableEnvironment(JavaPlugin plugin, String name)
	{
		super(plugin);
		
		this.named = name;
	}
	
	public abstract void enable();
	
	public abstract void disable();
	
	@Override
	public void onEnable()
	{
		controllers = new GList<Controllable>();
		timings = new GMap<Controllable, Integer>();
		liveTimings = new GMap<Controllable, Integer>();
		time = new Average(12);
		d = new D(getNamed());
		
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
		Phantom.instance().bindController(this);
		
		d.s("Started");
	}
	
	public String getNamed()
	{
		return named;
	}
	
	/**
	 * Get all controllers in this plugin
	 * 
	 * @return a GList of controllers
	 */
	public GList<Controllable> getAllControllers()
	{
		return getAllControllers(this);
	}
	
	/**
	 * Get all sub controllers from the given controller. This is a deep search
	 * (ALL
	 * sub sub controllers)
	 * 
	 * @param cx
	 *            the controllable object
	 * @return the list of controllers.
	 */
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
		if(getClass().isAnnotationPresent(SyncStart.class) || Phantom.syncStart())
		{
			T tx = new T()
			{
				@Override
				public void onStop(long nsTime, double msTime)
				{
					o("Started in " + F.f(msTime, 2) + "ms");
					Phantom.sm += msTime;
				}
			};
			
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
			
			tx.stop();
		}
		
		else
		{
			T tx = new T()
			{
				@Override
				public void onStop(long nsTime, double msTime)
				{
					o("Started in " + F.f(msTime, 2) + "ms");
					Phantom.am += msTime;
				}
			};
			
			new A()
			{
				@Override
				public void async()
				{
					for(Controllable i : controllers)
					{
						try
						{
							i.start();
						}
						
						catch(Exception e)
						{
							w(C.RED + "FAILED TO ASYNC LOAD! " + C.YELLOW + "Attempting to SYNC LOAD " + i.toString());
							
							new S()
							{
								@Override
								public void sync()
								{
									try
									{
										i.start();
									}
									
									catch(Exception ex)
									{
										f("FAILED TO SYNC START " + i.toString());
									}
								}
							};
						}
					}
					
					new S()
					{
						@Override
						public void sync()
						{
							onStart();
							
							task = new Task(ControllableEnvironment.this, 0)
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
							
							tx.stop();
						}
					};
				}
			};
		}
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
		try
		{
			onTick();
		}
		
		catch(Exception e)
		{
			f("Failed to tick " + getName() + " <> " + e.getClass().getSimpleName());
			ExceptionUtil.print(e);
		}
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
		return null;
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
	
	public Network getNetwork()
	{
		return Phantom.getBungeeNetwork();
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
	
	@Override
	public boolean isTicked()
	{
		return getClass().isAnnotationPresent(Ticked.class);
	}
	
	@Override
	public void onPreStart()
	{
		
	}
	
	@Override
	public void onPostStop()
	{
		
	}
	
	@Override
	public void onLoadComplete()
	{
		
	}
	
	@Override
	public void onPluginsComplete()
	{
		
	}
	
	@Override
	public ControllerMessage sendMessage(Controllable controller, ControllerMessage message)
	{
		return controller.onControllerMessageRecieved(message.copy());
	}
	
	@Override
	public ControllerMessage sendMessage(String controller, ControllerMessage message)
	{
		Controllable c = getController(controller);
		
		if(c != null)
		{
			return c.onControllerMessageRecieved(message.copy());
		}
		
		return null;
	}
	
	@Override
	public ControllerMessage onControllerMessageRecieved(ControllerMessage message)
	{
		return message;
	}
	
	@Override
	public Controllable getController(String name)
	{
		return Phantom.instance().getBinding(name);
	}
	
	/**
	 * Dispatcher helpers for using the internal dispatcher with this object
	 * simply invokes d.x()
	 * 
	 * @param s
	 *            the strings of messages
	 */
	public void i(String... s)
	{
		d.i(s);
	}
	
	/**
	 * Dispatcher helpers for using the internal dispatcher with this object
	 * simply invokes d.x()
	 * 
	 * @param s
	 *            the strings of messages
	 */
	public void s(String... o)
	{
		d.s(o);
	}
	
	/**
	 * Dispatcher helpers for using the internal dispatcher with this object
	 * simply invokes d.x()
	 * 
	 * @param s
	 *            the strings of messages
	 */
	public void f(String... o)
	{
		d.f(o);
	}
	
	/**
	 * Dispatcher helpers for using the internal dispatcher with this object
	 * simply invokes d.x()
	 * 
	 * @param s
	 *            the strings of messages
	 */
	public void w(String... o)
	{
		d.w(o);
	}
	
	/**
	 * Dispatcher helpers for using the internal dispatcher with this object
	 * simply invokes d.x()
	 * 
	 * @param s
	 *            the strings of messages
	 */
	public void v(String... o)
	{
		d.v(o);
	}
	
	/**
	 * Dispatcher helpers for using the internal dispatcher with this object
	 * simply invokes d.x()
	 * 
	 * @param s
	 *            the strings of messages
	 */
	public void o(String... o)
	{
		d.o(o);
	}

}

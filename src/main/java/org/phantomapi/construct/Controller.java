package org.phantomapi.construct;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.phantomapi.Phantom;
import org.phantomapi.async.AsyncWorker;
import org.phantomapi.clust.AsyncConfig;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurationHandler;
import org.phantomapi.clust.HandledConfig;
import org.phantomapi.command.CommandListener;
import org.phantomapi.core.DevelopmentController;
import org.phantomapi.core.SyncStart;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GList;
import org.phantomapi.network.Network;
import org.phantomapi.sync.S;
import org.phantomapi.util.Average;
import org.phantomapi.util.D;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.Probe;
import org.phantomapi.util.T;
import org.phantomapi.util.Timer;

/**
 * A controller
 * 
 * @author cyberpwn
 */
public abstract class Controller implements Controllable, ControllerMessenger
{
	protected final GList<Controllable> controllers;
	protected final Controllable parentController;
	protected final ControllablePlugin instance;
	protected final String name;
	protected final D d;
	private final Average time;
	
	/**
	 * Create a controller as a subcontroller from the parent controller. Tick
	 * rates ARE AFFECTED. For example, if the parent controler tickrate is one
	 * tick per second (20tps), and your tickrate is 0 or 1, your tick rate will
	 * actually be 1 tick per second instead of every tick. This is because the
	 * parent controller ticks your controller. Also, if the parent controller
	 * is not ticked, then this controller can not tick even with the ticked
	 * annotation
	 * 
	 * @param parentController
	 *            the parent controller
	 */
	public Controller(Controllable parentController)
	{
		controllers = new GList<Controllable>();
		this.parentController = parentController;
		name = getClass().getSimpleName();
		instance = parentController.getPlugin();
		d = new D(getPlugin().getName() + " > " + getName());
		time = new Average(8);
	}
	
	@Override
	public void start()
	{
		if(getClass().isAnnotationPresent(SyncStart.class) && Phantom.isAsync())
		{
			new S()
			{
				@Override
				public void sync()
				{
					start();
				}
			};
			
			return;
		}
		
		for(Controllable i : controllers)
		{
			i.start();
		}
		
		T tx = null;
		
		if(Phantom.isSync())
		{
			tx = new T()
			{
				@Override
				public void onStop(long nsTime, double msTime)
				{
					Phantom.sm += msTime;
				}
			};
		}
		
		getPlugin().registerListener(this);
		Phantom.registerSilenced(d);
		s("Started");
		onStart();
		
		if(tx != null)
		{
			tx.stop();
		}
	}
	
	@Override
	public void stop()
	{
		for(Controllable i : controllers)
		{
			i.stop();
		}
		
		getPlugin().unRegisterListener(this);
		s(ChatColor.RED + "Stopped");
		
		try
		{
			Phantom.instance().unbindController(this);
		}
		
		catch(Exception e)
		{
			
		}
		
		if(this instanceof Probe)
		{
			Phantom.instance().getProbeController().unRegisterProbe((Probe) this);
		}
		
		if(this instanceof CommandListener)
		{
			Phantom.instance().getCommandRegistryController().unregister((CommandListener) this);
		}
		
		Phantom.instance().getCommandRegistryController().unregister(this);
		
		onStop();
	}
	
	@Override
	public void tick()
	{
		Timer t = new Timer();
		t.start();
		onTick();
		t.stop();
		time.put(t.getTime());
		DevelopmentController.ticks++;
		DevelopmentController.timex += t.getTime();
	}
	
	/**
	 * Load a data cluster from file This will also create the file and add in
	 * default values if it doesnt exist
	 * 
	 * @param c
	 *            the configurable object
	 */
	public void loadCluster(Configurable c)
	{
		loadCluster(c, null);
	}
	
	/**
	 * Check if the current thread is async
	 * 
	 * @return true if it is
	 */
	public boolean isAsync()
	{
		return Phantom.isAsync();
	}
	
	/**
	 * Check if the current thread is sync
	 * 
	 * @return true if it is
	 */
	public boolean isSync()
	{
		return Phantom.isSync();
	}
	
	/**
	 * Online players of the server
	 * 
	 * @return the online players
	 */
	public GList<Player> onlinePlayers()
	{
		return Phantom.instance().onlinePlayers();
	}
	
	/**
	 * Load a data cluster from file This will also create the file and add in
	 * default values if it doesnt exist
	 * 
	 * @param c
	 *            the configurable object
	 * @param category
	 *            the category
	 */
	public void loadCluster(Configurable c, String category)
	{
		if(!c.getClass().isAnnotationPresent(HandledConfig.class))
		{
			Phantom.instance().getDms().getConfigurationBackupController().handle(this, c, category);
		}
		
		File base = getPlugin().getDataFolder();
		
		if(category != null)
		{
			base = new File(base, category);
		}
		
		try
		{
			if(c.getClass().isAnnotationPresent(AsyncConfig.class))
			{
				final File abase = base;
				v("@Async Loading " + c.getCodeName());
				
				new AsyncWorker()
				{
					@Override
					public void prepare()
					{
						
					}
					
					@Override
					public void finish()
					{
						v("Finished loading " + c.getCodeName());
					}
					
					@Override
					public void doWork()
					{
						try
						{
							ConfigurationHandler.read(abase, c);
						}
						
						catch(IOException e)
						{
							ExceptionUtil.print(e);
						}
					}
				}.start();
			}
			
			else
			{
				File b = base;
				
				new S()
				{
					@Override
					public void sync()
					{
						try
						{
							ConfigurationHandler.read(b, c);
						}
						
						catch(IOException e)
						{
							ExceptionUtil.print(e);
						}
					}
				};
			}
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
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
			f("No Tabled annotation for the configurable object " + c.getClass().getSimpleName() + "<" + c.getCodeName() + ">");
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
			f("No Tabled annotation for the configurable object " + c.getClass().getSimpleName() + "<" + c.getCodeName() + ">");
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
			f("No Tabled annotation for the configurable object " + c.getClass().getSimpleName() + "<" + c.getCodeName() + ">");
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
			f("No Tabled annotation for the configurable object " + c.getClass().getSimpleName() + "<" + c.getCodeName() + ">");
			return;
		}
		
		Phantom.instance().saveSql(c, finish);
	}
	
	/**
	 * Call an event
	 * 
	 * @param evt
	 *            the event
	 */
	public void callEvent(Event evt)
	{
		getPlugin().getServer().getPluginManager().callEvent(evt);
	}
	
	/**
	 * save a data cluster to the file This will also create the file and add in
	 * default values if it doesnt exist. New values will be added in aswell for
	 * updating configs
	 * 
	 * @param c
	 *            the configurable object
	 */
	public void saveCluster(Configurable c)
	{
		saveCluster(c, null);
	}
	
	/**
	 * save a data cluster to the file This will also create the file and add in
	 * default values if it doesnt exist. New values will be added in aswell for
	 * updating configs
	 * 
	 * @param c
	 *            the configurable object
	 * @param category
	 *            the category
	 */
	public void saveCluster(Configurable c, String category)
	{
		File base = getPlugin().getDataFolder();
		
		if(category != null)
		{
			base = new File(base, category);
		}
		
		try
		{
			ConfigurationHandler.save(base, c);
		}
		
		catch(IOException e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Queue a notification to a given player
	 * 
	 * @param p
	 *            the player
	 * @param n
	 *            the notification
	 */
	public void queueNotification(Player p, Notification n)
	{
		Phantom.queueNotification(p, n);
	}
	
	/**
	 * Queue a notification to all players
	 * 
	 * @param n
	 *            the notification
	 */
	public void queueNotification(Notification n)
	{
		Phantom.queueNotification(n);
	}
	
	public Network getNetwork()
	{
		return Phantom.getBungeeNetwork();
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
		
		if(c instanceof Probe)
		{
			Phantom.instance().getProbeController().registerProbe((Probe) c);
		}
	}
	
	@Override
	public void unregister(Controllable c)
	{
		controllers.remove(c);
	}
	
	@Override
	public abstract void onStart();
	
	@Override
	public abstract void onStop();
	
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
		return instance;
	}
	
	@Override
	public Controllable getParentController()
	{
		return parentController;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		Controllable c = this;
		GList<String> names = new GList<String>();
		
		while(c.getParentController() != null)
		{
			names.add(c.getName());
			c = c.getParentController();
		}
		
		names.add(getPlugin().getName());
		Collections.reverse(names);
		
		return names.toString(" > ");
	}
	
	public void i(String... s)
	{
		d.i(s);
	}
	
	public void s(String... o)
	{
		d.s(o);
	}
	
	public void f(String... o)
	{
		d.f(o);
	}
	
	public void w(String... o)
	{
		d.w(o);
	}
	
	public void v(String... o)
	{
		d.v(o);
	}
	
	public void o(String... o)
	{
		d.o(o);
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
		onStop();
		onStart();
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
	public ControllerMessage onControllerMessageRecieved(ControllerMessage message)
	{
		return message;
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
	public Controllable getController(String name)
	{
		return Phantom.instance().getBinding(name);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((controllers == null) ? 0 : controllers.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parentController == null) ? 0 : parentController.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		Controller other = (Controller) obj;
		
		if(controllers == null)
		{
			if(other.controllers != null)
			{
				return false;
			}
		}
		
		else if(!controllers.equals(other.controllers))
		{
			return false;
		}
		
		if(d == null)
		{
			if(other.d != null)
			{
				return false;
			}
		}
		
		else if(!d.equals(other.d))
		{
			return false;
		}
		
		if(instance == null)
		{
			if(other.instance != null)
			{
				return false;
			}
		}
		
		else if(!instance.equals(other.instance))
		{
			return false;
		}
		
		if(name == null)
		{
			if(other.name != null)
			{
				return false;
			}
		}
		
		else if(!name.equals(other.name))
		{
			return false;
		}
		
		if(parentController == null)
		{
			if(other.parentController != null)
			{
				return false;
			}
		}
		
		else if(!parentController.equals(other.parentController))
		{
			return false;
		}
		
		if(time == null)
		{
			if(other.time != null)
			{
				return false;
			}
		}
		
		else if(!time.equals(other.time))
		{
			return false;
		}
		
		return true;
	}
}

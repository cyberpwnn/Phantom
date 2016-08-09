package org.cyberpwn.phantom;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.DataCluster;
import org.cyberpwn.phantom.clust.JSONDataInput;
import org.cyberpwn.phantom.clust.JSONDataOutput;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.PhantomPlugin;
import org.cyberpwn.phantom.gui.Notification;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.sync.ExecutiveIterator;
import org.cyberpwn.phantom.transmit.Transmission;
import org.cyberpwn.phantom.transmit.TransmissionListener;
import org.cyberpwn.phantom.util.C;
import org.cyberpwn.phantom.util.D;
import org.cyberpwn.phantom.util.F;
import org.cyberpwn.phantom.util.SQLOperation;

/**
 * The Phantom Plugin object.
 * 
 * @author cyberpwn
 */
public class Phantom extends PhantomPlugin
{
	private static Phantom instance;
	private DataCluster environment;
	private ChanneledExecutivePoolController channeledExecutivePoolController;
	private TestController testController;
	private NotificationController notificationController;
	private DevelopmentController developmentController;
	private MySQLConnectionController mySQLConnectionController;
	private ProtocolController protocolController;
	private EventRippler eventRippler;
	private DMS dms;
	private TransmissionController transmissionController;
	private GList<Controllable> bindings;
	private GList<Plugin> plugins;
	private File envFile;
	
	public void enable()
	{
		instance = this;
		
		developmentController = new DevelopmentController(this);		
		environment = new DataCluster();
		dms = new DMS(this);
		testController = new TestController(this);
		channeledExecutivePoolController = new ChanneledExecutivePoolController(this);
		notificationController = new NotificationController(this);
		protocolController = new ProtocolController(this);
		mySQLConnectionController = new MySQLConnectionController(this);
		eventRippler = new EventRippler(this);
		transmissionController = new TransmissionController(this);
		plugins = new GList<Plugin>();
		bindings = new GList<Controllable>();
		
		register(developmentController);
		register(testController);
		register(channeledExecutivePoolController);
		register(notificationController);
		register(mySQLConnectionController);
		register(dms);
		register(eventRippler);
		register(protocolController);
		register(transmissionController);
		envFile = new File(getDataFolder().getParentFile().getParentFile(), "phantom-environment.json");
	}
	
	public void onStart()
	{
		try
		{
			new JSONDataInput().load(environment, envFile);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		GList<String> plx = new GList<String>();
		GList<String> pln = new GList<String>();
		
		for(Plugin i : Bukkit.getPluginManager().getPlugins())
		{
			if(i.getDescription().getDepend().contains("Phantom"))
			{
				plx.add(i.getName() + " v" + i.getDescription().getVersion());
				registerPlugin(i);
			}
			
			pln.add(i.getName() + " v" + i.getDescription().getVersion());
		}
		
		setEnvironmentData(this, "depending-plugins", plx);
		setEnvironmentData(this, "all-plugins", pln);
		setEnvironmentData(this, "api-revision", getDescription().getVersion());
		
		if(!getEnvironmentData().contains("phantom-status-database-failure"))
		{
			setEnvironmentData(this, "status-database-failure", false);
		}
		
		if(!getEnvironmentData().contains("phantom-status-data-failure"))
		{
			setEnvironmentData(this, "status-data-failure", false);
		}
		
		if(!getEnvironmentData().contains("phantom-status-plugin-failure"))
		{
			setEnvironmentData(this, "status-plugin-failure", false);
		}
		
		if(!getEnvironmentData().contains("phantom-status-api-failure"))
		{
			setEnvironmentData(this, "status-api-failure", false);
		}
		
		if(!getEnvironmentData().contains("phantom-status-network-failure"))
		{
			setEnvironmentData(this, "status-network-failure", false);
		}
		
		try
		{
			new JSONDataOutput().save(environment, envFile);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void onStop()
	{
		try
		{
			new JSONDataOutput().save(environment, envFile);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void disable()
	{
		
	}
	
	/**
	 * Get environment data shared by plugins
	 * 
	 * @return DataCluster containing variables
	 */
	public DataCluster getEnvironmentData()
	{
		return new DataCluster(environment.getData());
	}
	
	/**
	 * Set environment variables
	 * 
	 * @param key
	 *            the key
	 * @param v
	 *            the value (make it a primitive, wrapper, string, or
	 *            List<String>
	 */
	public void setEnvironmentData(Plugin source, String key, Object v)
	{
		environment.trySet(source.getName().toLowerCase() + "-" + key, v);
	}
	
	/**
	 * Register a plugin
	 * 
	 * @param i
	 *            the plugin
	 */
	public void registerPlugin(Plugin i)
	{
		plugins.add(i);
	}
	
	/**
	 * Schedule an iterator to be run
	 * 
	 * @param channel
	 *            the channel executor name
	 * @param it
	 *            the iterator
	 */
	public static void schedule(String channel, ExecutiveIterator<?> it)
	{
		instance.channeledExecutivePoolController.fire(channel, it);
	}
	
	/**
	 * Schedule an iterator to be run on the default scheduled executor
	 * 
	 * @param it
	 *            the iterator
	 */
	public static void schedule(ExecutiveIterator<?> it)
	{
		instance.channeledExecutivePoolController.fire("default", it);
	}
	
	/**
	 * Queue a notification
	 * 
	 * @param p
	 *            the player
	 * @param n
	 *            the notification
	 */
	public static void queueNotification(Player p, Notification n)
	{
		instance.notificationController.queue(p, n);
	}
	
	/**
	 * Schedule a notificiation to be played to everyone
	 * 
	 * @param n
	 *            the notificiation
	 */
	public static void queueNotification(Notification n)
	{
		instance.notificationController.queue(n);
	}
	
	public static void registerSilenced(D d)
	{
		instance.developmentController.registerSilencable(d.getName());
	}
	
	public static boolean isSilenced(D d)
	{
		return instance.developmentController.isQuiet(d.getName());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equals("phantom"))
		{
			if(sender.hasPermission("phantom.developer"))
			{
				if(args.length > 0)
				{
					if(args[0].equalsIgnoreCase("test") || args[0].equalsIgnoreCase("t"))
					{
						if(args.length == 2)
						{
							testController.execute(sender, args[1]);
						}
						
						else
						{
							sender.sendMessage(ChatColor.GREEN + testController.getTests().k().toString(", "));
						}
					}
					
					if(args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("s"))
					{
						printBindings(sender);
						
						sender.sendMessage(C.RED + "How's it look doc?");
						sender.sendMessage(C.AQUA + "Controllers: " + C.GREEN + F.f(getBindings().size()));
						
						double highest = 0;
						Controllable ccc = null;
						
						for(Controllable i : bindings.copy().qdel(this))
						{
							if(i.getTime() > highest)
							{
								highest = i.getTime();
								ccc = i;
							}
						}
						
						sender.sendMessage(C.AQUA + "Highest: " + C.GREEN + ccc.getClass().getSimpleName() + "(" + F.nsMs((long) highest, 4) + "ms)");
					}
				}
				
				else
				{
					sender.sendMessage(C.DARK_GRAY + "Phantom " + C.LIGHT_PURPLE + "v" + getDescription().getVersion());
				}
			}
			
			else
			{
				sender.sendMessage(C.DARK_GRAY + "Phantom " + C.LIGHT_PURPLE + "v" + getDescription().getVersion());
			}
		}
		
		return false;
	}
	
	/**
	 * Print all bindings
	 * 
	 * @param sender
	 *            the sender
	 */
	public void printBindings(CommandSender sender)
	{
		for(Controllable i : bindings)
		{
			if(i.getParentController() == null)
			{
				printBindings(sender, i, 0);
			}
		}
	}
	
	private void printBindings(CommandSender sender, Controllable c, int ind)
	{
		sender.sendMessage(StringUtils.repeat(" ", ind) + C.GREEN + c.getClass().getSimpleName() + ": " + C.AQUA + F.nsMs((long) c.getTime(), 2) + "ms");
		
		for(Controllable i : c.getControllers())
		{
			printBindings(sender, i, ind + 1);
		}
	}
	
	/**
	 * Log all bound controllers
	 * 
	 * @param c
	 *            the controller dispatcher
	 */
	public void logBindings(D c)
	{
		for(Controllable i : bindings)
		{
			if(i.getParentController() == null)
			{
				printBindings(c, i, 0);
			}
		}
	}
	
	private void printBindings(D cx, Controllable c, int ind)
	{
		cx.s(StringUtils.repeat(" ", ind) + C.GREEN + c.getClass().getSimpleName() + ": " + C.AQUA + F.nsMs((long) c.getTime(), 2) + "ms");
		
		for(Controllable i : c.getControllers())
		{
			printBindings(cx, i, ind + 1);
		}
	}
	
	/**
	 * Grab the instance of Phantom
	 * 
	 * @return phantom instance
	 */
	public static Phantom instance()
	{
		return instance;
	}
	
	/**
	 * Register a transmission listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void registerTransmitter(TransmissionListener listener)
	{
		transmissionController.registerListener(listener);
	}
	
	/**
	 * Transmit a packet
	 * 
	 * @param packet
	 *            the packet
	 */
	public void transmitPacket(Transmission packet)
	{
		transmissionController.transmitPacket(packet);
	}
	
	/**
	 * Request to save data from the cluster into the defined database. If the
	 * database is not defined, data wont be saved.
	 * 
	 * @param c
	 *            the configurable object to save
	 * @param finish
	 *            the runnable (when its been executed. Usually the same tick or
	 *            the one after)
	 */
	public void saveSql(Configurable c, Runnable finish)
	{
		mySQLConnectionController.queue(SQLOperation.SAVE, c, finish);
	}
	
	/**
	 * Request to load data from the cluster into the defined database. If the
	 * database is not defined, data wont be loaded into the cluster.
	 * 
	 * @param c
	 *            the configurable object to save
	 * @param finish
	 *            the runnable (when its been executed. Usually the same tick or
	 *            the one after)
	 */
	public void loadSql(Configurable c, Runnable finish)
	{
		mySQLConnectionController.queue(SQLOperation.LOAD, c, finish);
	}
	
	public MySQLConnectionController getMySQLConnectionController()
	{
		return mySQLConnectionController;
	}
	
	public void bindController(Controllable c)
	{
		bindings.add(c);
	}
	
	public static Phantom getInstance()
	{
		return instance;
	}
	
	public DataCluster getEnvironment()
	{
		return environment;
	}
	
	public ChanneledExecutivePoolController getChanneledExecutivePoolController()
	{
		return channeledExecutivePoolController;
	}
	
	public TestController getTestController()
	{
		return testController;
	}
	
	public NotificationController getNotificationController()
	{
		return notificationController;
	}
	
	public DevelopmentController getDevelopmentController()
	{
		return developmentController;
	}
	
	public EventRippler getEventRippler()
	{
		return eventRippler;
	}
	
	public DMS getDms()
	{
		return dms;
	}
	
	public GList<Controllable> getBindings()
	{
		return bindings;
	}
	
	public GList<Plugin> getPlugins()
	{
		return plugins;
	}
	
	public File getEnvFile()
	{
		return envFile;
	}
	
	public ProtocolController getProtocolController()
	{
		return protocolController;
	}
	
	public TransmissionController getTransmissionController()
	{
		return transmissionController;
	}
}

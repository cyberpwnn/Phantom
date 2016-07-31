package org.cyberpwn.phantom;

import java.io.File;
import java.io.IOException;

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
import org.cyberpwn.phantom.construct.PhantomPlugin;
import org.cyberpwn.phantom.gui.Notification;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.sync.ExecutiveIterator;
import org.cyberpwn.phantom.util.C;
import org.cyberpwn.phantom.util.SQLOperation;

/**
 * The Phantom Plugin object.
 * 
 * @author cyberpwn
 *
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
	private DMS dms;
	private GList<Plugin> plugins;
	private File envFile;
	
	public void enable()
	{	
		environment = new DataCluster();
		dms = new DMS(this);
		testController = new TestController(this);
		channeledExecutivePoolController = new ChanneledExecutivePoolController(this);
		developmentController = new DevelopmentController(this);
		notificationController = new NotificationController(this);
		mySQLConnectionController = new MySQLConnectionController(this);
		plugins = new GList<Plugin>();
		
		register(developmentController);
		register(testController);
		register(channeledExecutivePoolController);
		register(notificationController);
		register(mySQLConnectionController);
		register(dms);
		instance = this;
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
	 * @param key the key
	 * @param v the value (make it a primitive, wrapper, string, or List<String>
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
	 * Grab the instance of Phantom
	 * 
	 * @return phantom instance
	 */
	public static Phantom instance()
	{
		return instance;
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
}

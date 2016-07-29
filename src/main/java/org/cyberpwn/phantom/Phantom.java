package org.cyberpwn.phantom;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.construct.ControllablePlugin;
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
	private ChanneledExecutivePoolController channeledExecutivePoolController;
	private TestController testController;
	private NotificationController notificationController;
	private DevelopmentController developmentController;
	private MySQLConnectionController mySQLConnectionController;
	private GList<ControllablePlugin> plugins;
	
	public void enable()
	{
		testController = new TestController(this);
		channeledExecutivePoolController = new ChanneledExecutivePoolController(this);
		developmentController = new DevelopmentController(this);
		notificationController = new NotificationController(this);
		mySQLConnectionController = new MySQLConnectionController(this);
		plugins = new GList<ControllablePlugin>();
		
		register(developmentController);
		register(testController);
		register(channeledExecutivePoolController);
		register(notificationController);
		register(mySQLConnectionController);
		instance = this;
	}
	
	public void disable()
	{
		
	}
	
	public void registerPlugin(ControllablePlugin c)
	{
		plugins.add(c);
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
}

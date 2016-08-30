package org.phantomapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONDataInput;
import org.phantomapi.clust.JSONDataOutput;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.PhantomPlugin;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GList;
import org.phantomapi.nms.NMSX;
import org.phantomapi.placeholder.PlaceholderHooker;
import org.phantomapi.registry.GlobalRegistry;
import org.phantomapi.sync.ExecutiveIterator;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.text.TagProvider;
import org.phantomapi.transmit.Transmission;
import org.phantomapi.transmit.Transmitter;
import org.phantomapi.util.C;
import org.phantomapi.util.D;
import org.phantomapi.util.F;
import org.phantomapi.util.PluginUtil;
import org.phantomapi.util.SQLOperation;
import org.phantomapi.util.Timer;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

/**
 * The Phantom Plugin object.
 * 
 * @author cyberpwn
 */
public class Phantom extends PhantomPlugin implements TagProvider
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
	private CommandRegistryController commandRegistryController;
	private DMS dms;
	private GList<Controllable> bindings;
	private GList<Plugin> plugins;
	private File envFile;
	private GList<String> msgx = new GList<String>();
	private GlobalRegistry globalRegistry;
	private DefaultController defaultController;
	private BungeeController bungeeController;
	private PlaceholderController placeholderController;
	
	public void enable()
	{
		instance = this;
		
		developmentController = new DevelopmentController(this);
		environment = new DataCluster();
		dms = new DMS(this);
		commandRegistryController = new CommandRegistryController(this);
		testController = new TestController(this);
		channeledExecutivePoolController = new ChanneledExecutivePoolController(this);
		notificationController = new NotificationController(this);
		protocolController = new ProtocolController(this);
		mySQLConnectionController = new MySQLConnectionController(this);
		eventRippler = new EventRippler(this);
		defaultController = new DefaultController(this);
		plugins = new GList<Plugin>();
		placeholderController = new PlaceholderController(this);
		bungeeController = new BungeeController(this);
		bindings = new GList<Controllable>();
		msgx = new GList<String>();
		new PlaceholderHooker(this, "phantom").hook();
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		register(developmentController);
		register(commandRegistryController);
		register(testController);
		register(channeledExecutivePoolController);
		register(notificationController);
		register(mySQLConnectionController);
		register(dms);
		register(eventRippler);
		register(protocolController);
		register(defaultController);
		register(bungeeController);
		register(placeholderController);
		
		envFile = new File(getDataFolder().getParentFile().getParentFile(), "phantom-environment.json");
		globalRegistry = new GlobalRegistry();
		
		msgx.add("Dammit, let's do something already.");
		msgx.add("Pump'd up and ready to fight.");
		msgx.add("Good to go. Lets do this.");
		msgx.add("What do you have in mind?");
		msgx.add("I'm ready. Are you?");
		msgx.add("Get on my level.");
		msgx.add("Hurry up already");
		msgx.add("How can i assist?");
		msgx.add("Need something?");
		msgx.add("Stop doing that. I'm buisy.");
		msgx.add("No one isn't salty.");
		msgx.add("Caff'd up and ready to fight.");
		msgx.add("Caff'd up and ready to brawl bukkit.");
		msgx.add("Caff'd up with a hint of salt.");
		msgx.add("Check my insides. Seriously. WTFPL");
		msgx.add("Im Open source. REALLY Open source");
		msgx.add("Get lost.");
		msgx.add("Got something on your mind?");
		msgx.add("Lets do this.");
		msgx.add("Get it done.");
		msgx.add("I Boo the Swift");
		msgx.add("I only listen to cyberpwn");
		msgx.add("You probobly dont even know.");
		msgx.add("What are you doing...");
		msgx.add("Seriously...");
		msgx.add("You are doing it wrong man.");
		msgx.add("That Word... I dont think you know the meaning.");
		msgx.add("Stop bothering me.");
		msgx.add("It'd be a real shame if i broke events.");
		msgx.add("Who knows what power i have.");
		msgx.add("So... What are you trying to do?");
		msgx.add("Enjoy an empty help message to piss you off.");
		msgx.add("I'm enjoying your confusion");
		msgx.add("You know, you should listen more often.");
		msgx.add("Put an X on it. Makes it cool");
		msgx.add("Will you quit it.");
		msgx.add("What is wrong with you?");
		msgx.add("You need to be fixed. I clearly dont.");
		msgx.add("Dont ask.");
		msgx.add("You cant stop can you.");
		msgx.add("That was the wrong command buddy.");
		msgx.add("Welcome to hell.");
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
		
		setEnvironmentData(this, "identify-server", developmentController.id);
		
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
	
	public void saveEnvironment()
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
	
	public static void pingServer(String server, PhantomCommandSender s)
	{
		Timer ti = new Timer();
		
		Transmission t = new Transmission("ping", server)
		{
			@Override
			public void onResponse(Transmission response)
			{
				ti.stop();
				s.sendMessage(C.GREEN + getServerName() + " <" + F.nsMs(ti.getTime(), 6) + "> " + response.getSource());
			}
		};
		
		try
		{
			ti.start();
			t.transmit();
		}
		
		catch(IOException e)
		{
			s.sendMessage(C.RED + e.getMessage());
		}
	}
	
	public void disable()
	{
		
	}
	
	public static String getServerName()
	{
		return instance.bungeeController.getServerName();
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
	 * Get the bungeecord server name
	 * 
	 * @return the name or null if not bungeecord
	 */
	public static String getBungeeNameName()
	{
		return instance.bungeeController.get().getString("this");
	}
	
	/**
	 * Register transmitter
	 * 
	 * @param t
	 *            the transmitter
	 */
	public static void registerTransmitter(Transmitter t)
	{
		instance.bungeeController.registerTransmitter(t);
	}
	
	/**
	 * Unregister transmitter
	 * 
	 * @param t
	 *            the transmitter
	 */
	public static void unregisterTransmitter(Transmitter t)
	{
		instance.bungeeController.unregisterTransmitter(t);
	}
	
	/**
	 * Is phantom connected on a bungeecord network?
	 * 
	 * @return true if bungee is detected
	 */
	public boolean isBungeecord()
	{
		return getServerName() != null && getBungeeController().connected();
	}
	
	/**
	 * Get the list of servers on this network
	 * 
	 * @return returns null if not a network
	 */
	public static List<String> getServers()
	{
		return instance.bungeeController.get().getStringList("servers");
	}
	
	/**
	 * Get the count of the network
	 * 
	 * @return returns 0 if not a network
	 */
	public static int getNetworkCount()
	{
		int c = 0;
		
		for(String i : instance.bungeeController.get().getStringList("servers"))
		{
			c += getNetworkCount(i);
		}
		
		return c;
	}
	
	/**
	 * Reload a controller
	 * 
	 * @param s
	 *            the controller
	 */
	public void reloadController(String s)
	{
		for(Controllable i : roots())
		{
			if(i.getName().equalsIgnoreCase(s))
			{
				i.reload();
			}
		}
	}
	
	/**
	 * Get all root controllers
	 * 
	 * @return the roots
	 */
	public GList<Controllable> roots()
	{
		GList<Controllable> c = new GList<Controllable>();
		
		for(Controllable i : bindings)
		{
			if(i.getParentController() == null)
			{
				c.add(i);
			}
		}
		
		return c;
	}
	
	/**
	 * Get the count of a server on the network
	 * 
	 * @param server
	 *            the server name
	 * @return returns 0 if not a server, not a network, or the number count
	 */
	public static int getNetworkCount(String server)
	{
		int c = 0;
		
		try
		{
			c += instance.bungeeController.get().getInt("server." + server + ".count");
		}
		
		catch(Exception e)
		{
			
		}
		
		return c;
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
	
	public Controllable getController(Plugin p)
	{
		if(isPhantomPlugin(p))
		{
			return (Controllable) p;
		}
		
		return null;
	}
	
	public boolean isPhantomPlugin(Plugin p)
	{
		return p instanceof PhantomPlugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equals("phantom"))
		{
			MessageBuilder mb = new MessageBuilder(this);
			
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
							mb.message(sender, C.GRAY + testController.getTests().k().toString(", "));
							mb.message(sender, C.GRAY + "");
						}
					}
					
					else if(args[0].equalsIgnoreCase("thrash"))
					{
						thrash(sender);
					}
					
					else if(args[0].equalsIgnoreCase("unload") || args[0].equalsIgnoreCase("disable"))
					{
						if(args.length == 2)
						{
							for(Plugin i : Bukkit.getPluginManager().getPlugins())
							{
								if(i.getName().equalsIgnoreCase(args[1]))
								{
									if(isPhantomPlugin(i))
									{
										sender.sendMessage(getChatTag() + C.BOLD + C.WHITE + i.getName() + C.GRAY + " Stopping");
									}
									
									PluginUtil.unload(i);
									sender.sendMessage(getChatTag() + C.BOLD + C.WHITE + i.getName() + C.GRAY + " Unloaded");
									
									return true;
								}
							}
							
							sender.sendMessage(getChatTag() + C.GRAY + "WHAT?");
						}
					}
					
					else if(args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("enable"))
					{
						if(args.length == 2)
						{
							if(PluginUtil.load(args[1]))
							{
								sender.sendMessage(getChatTag() + C.BOLD + C.WHITE + args[1] + C.GRAY + " Loaded");
								return true;
							}
							
							sender.sendMessage(getChatTag() + C.GRAY + "WHAT?");
						}
					}
					
					else if(args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("s"))
					{
						mb.message(sender, C.GRAY + "How's it look doc?");
						
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
						
						mb.message(sender, C.GRAY + "Highest: " + C.WHITE + ccc.getClass().getSimpleName() + "(" + F.nsMs((long) highest, 4) + "ms)");
						sender.sendMessage(getChatTag() + C.GRAY + "Status: " + C.WHITE + status().paste() + ".js");
						sender.sendMessage(getChatTag() + C.GRAY + "Network: " + C.WHITE + getBungeeController().get().paste() + ".js");
					}
					
					else if(args[0].equalsIgnoreCase("network") || args[0].equalsIgnoreCase("n"))
					{
						PhantomSender s = new PhantomSender(sender);
						s.setMessageBuilder(new MessageBuilder(this));
						
						if(getServers() == null)
						{
							s.sendMessage(C.RED + "No Servers");
							return true;
						}
						
						for(String i : getServers())
						{
							if(i.equals(getServerName()))
							{
								continue;
							}
							
							pingServer(i, s);
						}
						
						return true;
					}
					
					else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r"))
					{
						if(args.length > 1)
						{
							for(Controllable i : roots())
							{
								if(i.getName().equalsIgnoreCase(args[1]))
								{
									sender.sendMessage(getChatTag() + C.GRAY + "Reloading " + C.WHITE + i.getName());
									i.reload();
									sender.sendMessage(getChatTag() + C.GRAY + "Reloaded " + C.WHITE + i.getName());
									return true;
								}
							}
						}
						
						GList<String> msg = new GList<String>();
						msg.add("Hmmmmmmm.... No");
						msg.add("Nope.");
						msg.add("Perhaps... Perhaps Not?");
						msg.add("Are you sure you know the implications of that?");
						msg.add("You really dont have any idea what your doing.");
						msg.add("Are you going to keep bothering me?");
						msg.add("Seriously, stop asking.");
						msg.add("You Annoy me, therefore i wont reload for you.");
						msg.add("Do it yourself. See what happens.");
						msg.add("I'd love to see you try and fail horribly.");
						msg.add("That's a terrible idea.");
						
						mb.message(sender, C.GRAY + msg.pickRandom());
					}
					
					else
					{
						GList<String> msg = new GList<String>();
						msg.add("Yeah... Suuuurrreee... Ok...");
						msg.add("Nope. You are terrible.");
						msg.add(C.GREEN + "Success!" + C.GRAY + "Just kidding. Wrong again.");
						msg.add("No. Stop it.");
						msg.add("You are failing terribly.");
						msg.add("You are interrupting my ability to tick controllers.");
						msg.add("Will you quit doing that?");
						msg.add("What is wrong with you?");
						msg.add("That's disgusting. Stop that at once.");
						msg.add("Hell no. Im not doing that.");
						msg.add("What are you talking about.");
						msg.add("Go to bed.");
						msg.add("The typo you just created almost killed me");
						msg.add("Are you trying to create exceptions?");
						msg.add("For the love of all that exists...");
						msg.add("Wh... But.... Stop.");
						msg.add("Bam! Wrong again.");
						msg.add("Go back to school");
						msg.add("SPEAK ENGLISH");
						
						mb.message(sender, C.GRAY + msg.pickRandom());
					}
				}
				
				else
				{
					mb.message(sender, C.WHITE + msgx.pickRandom());
					mb.message(sender, C.GRAY + "/p,pha,phantom" + C.GRAY + " - The Beginning");
					mb.message(sender, C.GRAY + "/phantom test,t" + C.GRAY + " - Run Tests");
					mb.message(sender, C.GRAY + "/phantom status,s" + C.GRAY + " - How's it look doc?");
					mb.message(sender, C.GRAY + "/phantom thrash" + C.GRAY + " - Reload Phantom");
				}
			}
			
			else
			{
				mb.message(sender, C.GRAY + msgx.pickRandom());
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private static void thrashUpdate(String s)
	{
		for(Player i : instance.onlinePlayers())
		{
			i.sendTitle(C.LIGHT_PURPLE + " ", ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Patching: " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + s);
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void thrashStart()
	{
		for(Player i : instance.onlinePlayers())
		{
			NMSX.clearTitle(i);
			i.sendTitle(C.LIGHT_PURPLE + " ", ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Please Wait");
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void thrashComplete()
	{
		for(Player i : instance.onlinePlayers())
		{
			i.sendTitle(C.LIGHT_PURPLE + " ", ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "UPDATE COMPLETE");
		}
	}
	
	public static void thrash(final CommandSender sender)
	{
		final String t = C.LIGHT_PURPLE + "[" + C.DARK_GRAY + "Phantom" + C.LIGHT_PURPLE + "]: " + C.GRAY;
		sender.sendMessage(t + "Preparing Thrash");
		
		boolean b = instance.developmentController.titles;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Phantom.instance, new Runnable()
		{
			@Override
			public void run()
			{
				if(b)
				{
					thrashStart();
				}
				
				sender.sendMessage(t + "Checking Plugins");
				List<String> thrashable = new ArrayList<String>();
				
				for(Plugin i : Bukkit.getPluginManager().getPlugins())
				{
					if(i instanceof PhantomPlugin && !i.getName().equals(Phantom.instance.getName()))
					{
						thrashable.add(i.getName());
						sender.sendMessage(t + "> " + i.getName() + " " + i.getDescription().getVersion() + " (Thrashable)");
					}
				}
				
				if(thrashable.isEmpty())
				{
					sender.sendMessage(t + C.RED + "No Thrashable Plugins found.");
				}
				
				else
				{
					sender.sendMessage(t + C.BOLD + "BEGINNING THRASH");
					
					int ic = 0;
					int imax = (thrashable.size() * 2) + 2;
					
					for(String i : thrashable)
					{
						PluginUtil.unloadNoGC(Bukkit.getPluginManager().getPlugin(i));
						ic++;
						sender.sendMessage(t + "Thrashing... " + C.BOLD + (int) (100.0 * ((double) ic / (double) imax)) + "%");
						
						if(b)
						{
							thrashUpdate((int) (100.0 * ((double) ic / (double) imax)) + "%");
						}
					}
					
					PluginUtil.unloadNoGC(Bukkit.getPluginManager().getPlugin("Phantom"));
					ic++;
					sender.sendMessage(t + "Thrashing... " + C.BOLD + (int) (100.0 * ((double) ic / (double) imax)) + "%");
					
					if(b)
					{
						thrashUpdate((int) (100.0 * ((double) ic / (double) imax)) + "%");
					}
					
					System.gc();
					
					PluginUtil.load("Phantom");
					ic++;
					sender.sendMessage(t + "Thrashing... " + C.BOLD + (int) (100.0 * ((double) ic / (double) imax)) + "%");
					
					if(b)
					{
						thrashUpdate((int) (100.0 * ((double) ic / (double) imax)) + "%");
					}
					
					for(String i : thrashable)
					{
						PluginUtil.load(i);
						ic++;
						sender.sendMessage(t + "Thrashing... " + C.BOLD + (int) (100.0 * ((double) ic / (double) imax)) + "%");
						
						if(b)
						{
							thrashUpdate((int) (100.0 * ((double) ic / (double) imax)) + "%");
						}
					}
					
					sender.sendMessage(t + C.BOLD + "THRASH COMPLETE");
					
					if(b)
					{
						thrashComplete();
					}
				}
			}
		}, 10);
	}
	
	public DataCluster status()
	{
		DataCluster cc = new DataCluster();
		
		for(Controllable i : getBindings())
		{
			String key = "controller." + i.toString().replaceAll(" > ", ".");
			
			if(i.isTicked())
			{
				cc.set(key + ".ms", i.getTime());
			}
			
			if((i instanceof CommandListener) && getCommandRegistryController().getRegistrants().contains((CommandListener) i))
			{
				CommandListener l = (CommandListener) i;
				GList<String> regi = new GList<String>(l.getCommandAliases()).qadd(l.getCommandName());
				
				cc.set(key + "command-listener.bound", regi);
				cc.set(key + "command-listener.tag-provider.tag", C.stripColor(l.getChatTag()));
				cc.set(key + "command-listener.tag-provider.hover", C.stripColor(l.getChatTagHover()));
			}
		}
		
		for(Plugin i : getServer().getPluginManager().getPlugins())
		{
			cc.set("plugin." + i.getName(), i.getDescription().getVersion());
		}
		
		cc.set("phantom.api-version", getDescription().getVersion());
		
		return cc;
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
	
	public ProtocolManager getProtocolLib()
	{
		return ProtocolLibrary.getProtocolManager();
	}
	
	public CommandRegistryController getCommandRegistryController()
	{
		return commandRegistryController;
	}
	
	public void unbindController(Controllable c)
	{
		bindings.remove(c);
	}
	
	@Override
	public String getChatTag()
	{
		return C.LIGHT_PURPLE + "[" + C.DARK_GRAY + "Phantom" + C.LIGHT_PURPLE + "]: " + C.GRAY;
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.LIGHT_PURPLE + "Phantom " + getDescription().getVersion();
	}
	
	/**
	 * Shortcut for global registry
	 * 
	 * @return global registry
	 */
	public static GlobalRegistry r()
	{
		return getRegistry();
	}
	
	/**
	 * Get the global registry
	 * 
	 * @return the registry
	 */
	public static GlobalRegistry getRegistry()
	{
		return instance.globalRegistry;
	}
	
	public BungeeController getBungeeController()
	{
		return bungeeController;
	}
	
	public GList<String> getMsgx()
	{
		return msgx;
	}
	
	public GlobalRegistry getGlobalRegistry()
	{
		return globalRegistry;
	}
	
	public DefaultController getDefaultController()
	{
		return defaultController;
	}
	
	public PlaceholderController getPlaceholderController()
	{
		return placeholderController;
	}
}

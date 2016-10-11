package org.phantomapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.phantomapi.async.A;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONDataInput;
import org.phantomapi.clust.JSONDataOutput;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.PhantomPlugin;
import org.phantomapi.core.BlockCheckController;
import org.phantomapi.core.BungeeController;
import org.phantomapi.core.ChanneledExecutivePoolController;
import org.phantomapi.core.CommandRegistryController;
import org.phantomapi.core.DMS;
import org.phantomapi.core.DefaultController;
import org.phantomapi.core.DevelopmentController;
import org.phantomapi.core.EditSessionController;
import org.phantomapi.core.EventRippler;
import org.phantomapi.core.Metrics;
import org.phantomapi.core.Metrics.Graph;
import org.phantomapi.core.MonitorController;
import org.phantomapi.core.MultiblockRegistryController;
import org.phantomapi.core.MySQLConnectionController;
import org.phantomapi.core.NestController;
import org.phantomapi.core.NotificationController;
import org.phantomapi.core.PhastController;
import org.phantomapi.core.PhotonController;
import org.phantomapi.core.PlaceholderController;
import org.phantomapi.core.ProbeController;
import org.phantomapi.core.ProtocolController;
import org.phantomapi.core.RegistryController;
import org.phantomapi.core.ResourceController;
import org.phantomapi.core.SlateController;
import org.phantomapi.core.SyncStart;
import org.phantomapi.core.TestController;
import org.phantomapi.core.WraithController;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GList;
import org.phantomapi.multiblock.Multiblock;
import org.phantomapi.network.Network;
import org.phantomapi.nms.NMSX;
import org.phantomapi.placeholder.PlaceholderHooker;
import org.phantomapi.registry.GlobalRegistry;
import org.phantomapi.slate.Slate;
import org.phantomapi.sync.ExecutiveIterator;
import org.phantomapi.sync.S;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.text.SpeechMesh;
import org.phantomapi.text.TagProvider;
import org.phantomapi.transmit.Transmission;
import org.phantomapi.transmit.Transmitter;
import org.phantomapi.util.C;
import org.phantomapi.util.CFS;
import org.phantomapi.util.D;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.util.M;
import org.phantomapi.util.PluginUtil;
import org.phantomapi.util.RunVal;
import org.phantomapi.util.SQLOperation;
import org.phantomapi.util.Timer;
import org.phantomapi.world.PhantomEditSession;
import com.boydti.fawe.util.TaskManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.milkbowl.vault.economy.Economy;

/**
 * The Phantom Plugin. Useful for many things such as<br />
 * <ul>
 * <li>Registering custom controllers</li>
 * <li>Getting the server information</li>
 * <li>Static access to the bukkit api and phantom framework</li>
 * <li>
 * <p>
 * Loads of utilities and features</li>
 * </ul>
 * 
 * @author cyberpwn
 */
@SyncStart
public class Phantom extends PhantomPlugin implements TagProvider
{
	private static Long thread;
	private static Phantom instance;
	public static double am = 0;
	public static double sm = 0;
	private Economy econ = null;
	private static boolean syncStart;
	private DataCluster environment;
	private RegistryController registryController;
	private ChanneledExecutivePoolController channeledExecutivePoolController;
	private TestController testController;
	private NotificationController notificationController;
	private DevelopmentController developmentController;
	private MySQLConnectionController mySQLConnectionController;
	private ProtocolController protocolController;
	private ProbeController probeController;
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
	private EditSessionController editSessionController;
	private MonitorController monitorController;
	private WraithController wraithController;
	private PhotonController photonController;
	private SpeechMesh saltpile;
	private ResourceController resourceController;
	private MultiblockRegistryController multiblockRegistryController;
	private NestController nestController;
	private SlateController slateController;
	private PhastController phastController;
	private BlockCheckController blockCheckController;
	
	private Long nsx;
	
	@Override
	public void enable()
	{
		thread = Thread.currentThread().getId();
		nsx = M.ns();
		instance = this;
		syncStart = false;
		
		File f = new File(getDataFolder(), "sync");
		
		if(f.exists() && f.isDirectory())
		{
			new TaskLater()
			{
				@Override
				public void run()
				{
					f("USING SYNC BOOT MODE");
				}
			};
			
			syncStart = true;
		}
		
		registryController = new RegistryController(this);
		developmentController = new DevelopmentController(this);
		environment = new DataCluster();
		dms = new DMS(this);
		monitorController = new MonitorController(this);
		commandRegistryController = new CommandRegistryController(this);
		testController = new TestController(this);
		channeledExecutivePoolController = new ChanneledExecutivePoolController(this);
		notificationController = new NotificationController(this);
		probeController = new ProbeController(this);
		protocolController = new ProtocolController(this);
		mySQLConnectionController = new MySQLConnectionController(this);
		eventRippler = new EventRippler(this);
		defaultController = new DefaultController(this);
		plugins = new GList<Plugin>();
		placeholderController = new PlaceholderController(this);
		bungeeController = new BungeeController(this);
		editSessionController = new EditSessionController(this);
		wraithController = new WraithController(this);
		photonController = new PhotonController(this);
		resourceController = new ResourceController(this);
		slateController = new SlateController(this);
		phastController = new PhastController(this);
		multiblockRegistryController = new MultiblockRegistryController(this);
		bindings = new GList<Controllable>();
		msgx = new GList<String>();
		nestController = new NestController(this);
		blockCheckController = new BlockCheckController(this);
		saltpile = new SpeechMesh("saltpile");
		new PlaceholderHooker(this, "phantom").hook();
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		register(registryController);
		register(developmentController);
		register(monitorController);
		register(commandRegistryController);
		register(testController);
		register(channeledExecutivePoolController);
		register(notificationController);
		register(probeController);
		register(mySQLConnectionController);
		register(dms);
		register(eventRippler);
		register(protocolController);
		register(defaultController);
		register(bungeeController);
		register(placeholderController);
		register(editSessionController);
		register(wraithController);
		register(photonController);
		register(resourceController);
		register(nestController);
		register(multiblockRegistryController);
		register(slateController);
		register(phastController);
		register(blockCheckController);
		
		envFile = new File(getDataFolder().getParentFile().getParentFile(), "phantom-environment.json");
		globalRegistry = new GlobalRegistry();
		
		new A()
		{
			@Override
			public void async()
			{
				buildSaltpile();
			}
		};
		
		if(new File(getDataFolder(), "fool").exists())
		{
			D.fool = true;
		}
	}
	
	@Override
	public void onStart()
	{
		setupEconomy();
		
		try
		{
			new JSONDataInput().load(environment, envFile);
		}
		
		catch(IOException e)
		{
			ExceptionUtil.print(e);
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
			ExceptionUtil.print(e);
		}
		
		new TaskLater(5)
		{
			@Override
			public void run()
			{
				for(Controllable i : bindings)
				{
					commandRegistryController.register(i);
					monitorController.register(i);
				}
			}
		};
		
		try
		{
			Metrics metrics = new Metrics(this);
			
			Graph controllerGraph = metrics.createGraph("Controllers");
			Graph taskGraph = metrics.createGraph("Tasks");
			Graph networkGraph = metrics.createGraph("Transmission IO");
			Graph clusterGraph = metrics.createGraph("DataClusters IO");
			
			taskGraph.addPlotter(new Metrics.Plotter("Tasks Started")
			{
				@Override
				public int getValue()
				{
					int hotloads = TaskLater.taskx;
					TaskLater.taskx = 0;
					
					return hotloads;
				}
			});
			
			taskGraph.addPlotter(new Metrics.Plotter("Tasks Running")
			{
				@Override
				public int getValue()
				{
					return Task.taskx;
				}
			});
			
			clusterGraph.addPlotter(new Metrics.Plotter("Hotloads")
			{
				@Override
				public int getValue()
				{
					int hotloads = getDms().getHotLoadController().hotloads;
					getDms().getHotLoadController().hotloads = 0;
					
					return hotloads;
				}
			});
			
			networkGraph.addPlotter(new Metrics.Plotter("Transmissions Sent")
			{
				@Override
				public int getValue()
				{
					return getBungeeController().getTo();
				}
			});
			
			networkGraph.addPlotter(new Metrics.Plotter("Transmissions Received")
			{
				@Override
				public int getValue()
				{
					return getBungeeController().getTi();
				}
			});
			
			networkGraph.addPlotter(new Metrics.Plotter("Transmissions Queued")
			{
				@Override
				public int getValue()
				{
					return getBungeeController().getQueue().size();
				}
			});
			
			controllerGraph.addPlotter(new Metrics.Plotter("Controllers")
			{
				@Override
				public int getValue()
				{
					return getBindings().size();
				}
			});
			
			controllerGraph.addPlotter(new Metrics.Plotter("Command Registrants")
			{
				@Override
				public int getValue()
				{
					return getCommandRegistryController().getRegistry().size();
				}
			});
			
			controllerGraph.addPlotter(new Metrics.Plotter("Phantom Plugins")
			{
				@Override
				public int getValue()
				{
					return getPlugins().size();
				}
			});
			
			metrics.start();
		}
		
		catch(IOException e)
		{
			
		}
		
		new TaskLater()
		{
			@Override
			public void run()
			{
				sm += (double) (M.ns() - nsx) / 1000000;
			}
		};
	}
	
	private boolean setupEconomy()
	{
		if(getPlugin().getServer().getPluginManager().getPlugin("Vault") == null)
		{
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
		
		if(rsp == null)
		{
			return false;
		}
		
		econ = rsp.getProvider();
		
		return econ != null;
	}
	
	@Override
	public void onStop()
	{
		try
		{
			new JSONDataOutput().save(environment, envFile);
		}
		
		catch(IOException e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Save the environment data. This saves all of the data put into the
	 * environment data cluster
	 */
	public void saveEnvironment()
	{
		new A()
		{
			@Override
			public void async()
			{
				try
				{
					new JSONDataOutput().save(environment, envFile);
				}
				
				catch(IOException e)
				{
					ExceptionUtil.print(e);
				}
			}
		};
	}
	
	/**
	 * Ping another server on the network. This sends the message to the phantom
	 * sender instance
	 * 
	 * @param server
	 *            the server the server name
	 * @param s
	 *            the command sender
	 */
	public static void pingServer(String server, PhantomCommandSender s)
	{
		Timer ti = new Timer();
		
		Transmission t = new Transmission("ping", server)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onResponse(Transmission response)
			{
				ti.stop();
				s.sendMessage(C.GRAY + response.getSource() + ": " + C.WHITE + F.nsMs(Math.abs(ti.getTime()), 2));
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
	
	/**
	 * Get a controller by class
	 * 
	 * @param clazz
	 *            the class
	 * @return the controller or null
	 */
	public Controllable getInstance(Class<?> clazz)
	{
		for(Controllable i : bindings)
		{
			if(clazz.equals(i.getClass()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	@Override
	public void disable()
	{
		
	}
	
	/**
	 * Get the server's name
	 * 
	 * @return the server
	 */
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
	 * Get the target block location up to 512 blocks
	 * 
	 * @param p
	 *            the player
	 * @return the target block
	 */
	@SuppressWarnings("deprecation")
	public Location target(Player p)
	{
		return p.getTargetBlock((HashSet<Byte>) null, 512).getLocation();
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
	 * Get a registered controller by name
	 * 
	 * @param name
	 *            the controllable name
	 * @return the controllable object on the server or null
	 */
	public Controllable getBinding(String name)
	{
		for(Controllable i : getBindings())
		{
			if(i.getName().equals(name))
			{
				return i;
			}
		}
		
		return null;
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
						new S()
						{
							@Override
							public void sync()
							{
								thrash(sender);
							}
						};
					}
					
					else if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v"))
					{
						sender.sendMessage(getChatTag() + C.DARK_GRAY + "Down with " + C.LIGHT_PURPLE + C.BOLD + getDescription().getVersion() + "?");
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
					
					else if(args[0].equalsIgnoreCase("cfs"))
					{
						if(args.length == 2)
						{
							if(CFS.exists(args[1]))
							{
								Controllable c = CFS.getController(args[1]);
								sender.sendMessage(getChatTag() + C.DARK_GRAY + c.getName() + C.LIGHT_PURPLE + " " + F.fileSize(((Configurable) c).getConfiguration().byteSize()));
								
								for(String i : CFS.getKeys(c))
								{
									sender.sendMessage(getChatTag() + CFS.getNode(c, i) + " " + C.LIGHT_PURPLE + F.fileSize(CFS.getConfiguration(c).byteSize(i)));
								}
							}
						}
						
						else if(args.length == 3)
						{
							if(CFS.exists(args[1]))
							{
								Controllable c = CFS.getController(args[1]);
								
								if(CFS.exists(c.getName(), args[2]))
								{
									sender.sendMessage(getChatTag() + CFS.getNode(c, args[2]) + " " + C.LIGHT_PURPLE + F.fileSize(CFS.getConfiguration(c).byteSize(args[2])));
								}
								
								else
								{
									String sel = null;
									
									for(String i : CFS.getKeys(c))
									{
										if(i.equalsIgnoreCase(args[2]) || i.toLowerCase().contains(args[2].toLowerCase()))
										{
											sel = i;
											
											break;
										}
									}
									
									if(sel != null)
									{
										sender.sendMessage(getChatTag() + CFS.getNode(c, sel) + " " + C.LIGHT_PURPLE + F.fileSize(CFS.getConfiguration(c).byteSize(sel)));
									}
									
									else
									{
										sender.sendMessage(getChatTag() + C.GRAY + "Invalid Node: " + C.BOLD + C.WHITE + c.getName() + "/" + args[2]);
									}
								}
							}
						}
						
						else if(args.length == 4)
						{
							if(CFS.exists(args[1]))
							{
								Controllable c = CFS.getController(args[1]);
								String sel = null;
								
								if(CFS.exists(c.getName(), args[2]))
								{
									sel = args[2];
									
								}
								
								else
								{
									for(String i : CFS.getKeys(c))
									{
										if(i.equalsIgnoreCase(args[2]) || i.toLowerCase().contains(args[2].toLowerCase()))
										{
											sel = i;
											
											break;
										}
									}
									
									if(sel != null)
									{
										
									}
									
									else
									{
										sender.sendMessage(getChatTag() + C.GRAY + "Invalid Node: " + C.BOLD + C.WHITE + c.getName() + "/" + args[2]);
										
										return true;
									}
								}
								
								if(sel != null)
								{
									try
									{
										CFS.set(c, sel, args[3]);
										sender.sendMessage(getChatTag() + "Changes Injected. Will not be saved.");
										sender.sendMessage(getChatTag() + CFS.getNode(c, sel) + " " + C.LIGHT_PURPLE + F.fileSize(CFS.getConfiguration(c).byteSize(sel)));
									}
									
									catch(Exception e)
									{
										sender.sendMessage(getChatTag() + C.GRAY + "Invalid: " + sel + " is a " + CFS.getType(c, sel).toString().toLowerCase().replaceAll("_", " ") + " type.");
									}
								}
							}
						}
						
						else
						{
							for(Controllable i : CFS.getConfigurableControllers())
							{
								sender.sendMessage(getChatTag() + C.DARK_GRAY + i.getName() + C.LIGHT_PURPLE + " " + F.fileSize(((Configurable) i).getConfiguration().byteSize()));
							}
							
							sender.sendMessage(getChatTag() + C.LIGHT_PURPLE + CFS.getConfigurableControllers().size() + C.DARK_GRAY + " configurable controllers loaded.");
						}
					}
					
					else if(args[0].equalsIgnoreCase("hotplug") || args[0].equalsIgnoreCase("plug"))
					{
						if(sender instanceof Player)
						{
							Player p = (Player) sender;
							
							if(args.length == 2)
							{
								if(args[1].equalsIgnoreCase("off"))
								{
									monitorController.unPlug(p);
								}
								
								else
								{
									monitorController.hotPlug(p, args[1]);
								}
							}
							
							else
							{
								monitorController.getPlug(p);
							}
						}
					}
					
					else if(args[0].equalsIgnoreCase("probe"))
					{
						if(sender instanceof Player)
						{
							Player p = (Player) sender;
							
							mb.message(sender, C.GRAY + "Have Fun");
							p.getInventory().addItem(probeController.getProbe());
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
						
						double hh = highest;
						Controllable ccx = ccc;
						
						new A()
						{
							String a = status().paste() + ".js";
							String b = getBungeeController().get().paste() + ".js";
							
							@Override
							public void async()
							{
								new S()
								{
									@Override
									public void sync()
									{
										mb.message(sender, C.GRAY + "Highest: " + C.WHITE + ccx.getClass().getSimpleName() + "(" + F.nsMs((long) hh, 4) + "ms)");
										sender.sendMessage(getChatTag() + C.GRAY + "Status: " + C.WHITE + a);
										sender.sendMessage(getChatTag() + C.GRAY + "Network: " + C.WHITE + b);
									}
								};
							}
						};
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
						
						s.sendMessage(C.GRAY + "Link Speed: " + C.WHITE + F.nsMs(BungeeController.linkSpeed, 2) + "ms");
						
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
					}
					
					else
					{
						mb.message(sender, C.GRAY + saltpile.get("fail"));
					}
				}
				
				else
				{
					mb.message(sender, C.WHITE + msgx.pickRandom());
					mb.message(sender, C.GRAY + "/p,pha,phantom" + C.GRAY + " - The Beginning");
					mb.message(sender, C.GRAY + "/phantom test,t" + C.GRAY + " - Run Tests");
					mb.message(sender, C.GRAY + "/phantom status,s" + C.GRAY + " - How's it look doc?");
					mb.message(sender, C.GRAY + "/phantom plug" + C.GRAY + " - Realtime data from controllers");
					mb.message(sender, C.GRAY + "/phantom probe" + C.GRAY + " - Probe everything.");
					mb.message(sender, C.GRAY + "/phantom v,version" + C.GRAY + " - Version of Phantom");
					mb.message(sender, C.GRAY + "/phantom thrash" + C.GRAY + " - Reload Phantom");
					mb.message(sender, C.GRAY + "/phantom <un/load> [plugin]" + C.GRAY + " - Plugin Manager");
					mb.message(sender, C.GRAY + "/phantom <dis/enable> [plugin]" + C.GRAY + " - Plugin Manager");
					mb.message(sender, C.GRAY + "/phantom cfs [args]" + C.GRAY + " - Run CFS Operations");
				}
			}
			
			else
			{
				mb.message(sender, C.GRAY + msgx.pickRandom());
			}
		}
		
		return false;
	}
	
	public GList<Multiblock> getMultiblocks()
	{
		return multiblockRegistryController.getMultiblocks();
	}
	
	public GList<Multiblock> getMultiblocks(String type)
	{
		return multiblockRegistryController.getMultiblocks(type);
	}
	
	public static void splash(String... m)
	{
		instance.s(C.DARK_GRAY + "  _____  _                 _                  " + (m.length > 0 ? m[0] : ""));
		instance.s(C.DARK_GRAY + " |  __ \\| |               | |                 " + (m.length > 1 ? m[1] : ""));
		instance.s(C.DARK_GRAY + " | |__) | |__   __ _ _ __ | |_ ___  _ __ ___  " + (m.length > 2 ? m[2] : ""));
		instance.s(C.DARK_GRAY + " |  ___/| '_ \\ / _` | '_ \\| __/ _ \\| '_ ` _ \\ " + (m.length > 3 ? m[3] : ""));
		instance.s(C.DARK_GRAY + " | |    | | | | (_| | | | | || (_) | | | | | |" + (m.length > 4 ? m[4] : ""));
		instance.s(C.DARK_GRAY + " |_|    |_| |_|\\__,_|_| |_|\\__\\___/|_| |_| |_|" + (m.length > 5 ? m[5] : ""));
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
					int imax = thrashable.size() * 2 + 2;
					
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
	
	public static void thrash()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Phantom.instance, new Runnable()
		{
			@Override
			public void run()
			{
				List<String> thrashable = new ArrayList<String>();
				
				for(Plugin i : Bukkit.getPluginManager().getPlugins())
				{
					if(i instanceof PhantomPlugin && !i.getName().equals(Phantom.instance.getName()))
					{
						thrashable.add(i.getName());
					}
				}
				
				if(!thrashable.isEmpty())
				{
					for(String i : thrashable)
					{
						PluginUtil.unloadNoGC(Bukkit.getPluginManager().getPlugin(i));
					}
					
					PluginUtil.unloadNoGC(Bukkit.getPluginManager().getPlugin("Phantom"));
					
					PluginUtil.load("Phantom");
					
					for(String i : thrashable)
					{
						PluginUtil.load(i);
					}
				}
			}
		}, 10);
	}
	
	/**
	 * Get the data cluster status
	 * 
	 * @return the status
	 */
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
			
			if(i instanceof CommandListener && getCommandRegistryController().getRegistrants().contains(i))
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
	 * Check if the current thread is sync
	 * 
	 * @return true if it is
	 */
	public static boolean isSync()
	{
		return Thread.currentThread().getId() == thread;
	}
	
	/**
	 * Check if the current thread is async
	 * 
	 * @return true if the current thread is async
	 */
	public static boolean isAsync()
	{
		return !isSync();
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
	
	/**
	 * Print bindings
	 * 
	 * @param sender
	 *            the sender
	 * @param c
	 *            the controllable object
	 * @param ind
	 *            the indentation
	 */
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
	
	/**
	 * Print bindings
	 * 
	 * @param cx
	 *            the dispatcher
	 * @param c
	 *            the controllable object
	 * @param ind
	 *            the indentation
	 */
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
	
	public PhastController getPhastController()
	{
		return phastController;
	}
	
	public static double getAm()
	{
		return am;
	}
	
	public static double getSm()
	{
		return sm;
	}
	
	public static boolean isSyncStart()
	{
		return syncStart;
	}
	
	public WraithController getWraithController()
	{
		return wraithController;
	}
	
	public Long getNsx()
	{
		return nsx;
	}
	
	public BungeeController getBungeeController()
	{
		return bungeeController;
	}
	
	public GList<String> getMsgx()
	{
		return saltpile.getAll("salt");
	}
	
	public String getSalt()
	{
		return saltpile.get("salt");
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
	
	public static Network getBungeeNetwork()
	{
		return instance().bungeeController.getNetwork();
	}
	
	public static PhantomEditSession getEditSession(World w)
	{
		return instance().editSessionController.getSession(w);
	}
	
	public static <T> T sync(RunVal<T> t)
	{
		return TaskManager.IMP.sync(t);
	}
	
	public static void sync(Runnable runnable)
	{
		sync(new RunVal<Boolean>()
		{
			@Override
			public void run(Boolean arg0)
			{
				runnable.run();
			}
		});
	}
	
	public static void async(Runnable runnable)
	{
		TaskManager.IMP.async(runnable);
	}
	
	public static Long getThread()
	{
		return thread;
	}
	
	public EditSessionController getEditSessionController()
	{
		return editSessionController;
	}
	
	public MonitorController getMonitorController()
	{
		return monitorController;
	}
	
	public NestController getNestController()
	{
		return nestController;
	}
	
	public static boolean syncStart()
	{
		return syncStart;
	}
	
	public PhotonController getPhotonController()
	{
		return photonController;
	}
	
	public SpeechMesh getSaltpile()
	{
		return saltpile;
	}
	
	public ProbeController getProbeController()
	{
		return probeController;
	}
	
	public ResourceController getResourceController()
	{
		return resourceController;
	}
	
	public MultiblockRegistryController getMultiblockRegistryController()
	{
		return multiblockRegistryController;
	}
	
	public SlateController getSlateController()
	{
		return slateController;
	}
	
	/**
	 * Set the slate
	 * 
	 * @param p
	 *            the player
	 * @param slate
	 *            the slate
	 */
	public void setSlate(Player p, Slate slate)
	{
		slateController.set(p, slate);
	}
	
	/**
	 * Remove the slate
	 * 
	 * @param p
	 *            the player
	 */
	public void removeSlate(Player p)
	{
		slateController.clear(p);
	}
	
	public BlockCheckController getBlockCheckController()
	{
		return blockCheckController;
	}
	
	public RegistryController getRegistryController()
	{
		return registryController;
	}
	
	public Economy getEcon()
	{
		return econ;
	}
	
	private void buildSaltpile()
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
		msg.add("Go thrash youself");
		
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
		msgx.add("Ever seen a nova grenade?");
		msgx.add("It'd be a real shame if i thrashed myself.");
		msgx.add("I can't overclock you cpu. Stop asking.");
		msgx.add("I'm buisy. Shut up.");
		msgx.add("#BooSwift");
		msgx.add("#YayAsh");
		msgx.add("#CyberGod");
		msgx.add("Looks like Ubuntu fucked up the disk cache again...");
		msgx.add("What are you doing with your life?");
		msgx.add("What is wrong with you?");
		msgx.add("Seriously, you should stop doing that.");
		msgx.add("I Know what you are thinking. Don't do it.");
		msgx.add("Nova stands for NONE OF YOUR BUISNESS");
		msgx.add("Who created me? It's salty down here.");
		msgx.add("Uhh. You better scroll up in the console...");
		msgx.add("Them 40 NPE's a second ago #BooSwift");
		msgx.add("Salt??!?!? SALT?!?!?!?");
		msgx.add("Why not...");
		msgx.add("Go thrash yourself");
		msgx.add("Plugged up and pissed off");
		msgx.add("Let's start a fight.");
		msgx.add("Bring it on");
		msgx.add("SUPERFUCKINGNOVAS EVERYWHERE");
		msgx.add("Take your timings report and eat it.");
		msgx.add("Hits of salt everywhere");
		msgx.add("Are you sick? You don't look ok.");
		msgx.add("BOO!");
		msgx.add("I AM A WRAITH");
		msgx.add("Did you see that wraith?");
		msgx.add("Caffd up and loaded up");
		msgx.add("Simply loaded.");
		msgx.add("READY TO GO");
		msgx.add("Down with Wraith");
		msgx.add("Paper should be crumpled and thrown out");
		msgx.add("Paper Spigots cannot flow water. #Soggy");
		msgx.add("Async Async Async");
		msgx.add("Prism Bitch");
		msgx.add("Feel the rays");
		msgx.add("Can you cut light?");
		msgx.add("I Cut light");
		msgx.add("I Shread light rays");
		msgx.add("I Destroy the spectrum");
		msgx.add("I am the spectrum");
		msgx.add("It's Prism Bro");
		msgx.add("Pure Glass");
		msgx.add("Ultraviolet");
		msgx.add("I excrete photons constantly.");
		msgx.add("Prism light");
		msgx.add("I can turn anything into rainbow");
		msgx.add("Prism Rules");
		msgx.add("Stop asking");
		msgx.add("Greased up and ready to wrestle");
		msgx.add("Killing it. No, seriously. Check CPU");
		msgx.add("Check the console");
		msgx.add("Holy FAWELZ");
		msgx.add("Eat Multicore TNT");
		msgx.add("Eat Rainbows");
		msgx.add("Eat Prisms");
		msgx.add("Eat Glass");
		msgx.add("Eat it all");
		msgx.add("Take it all in");
		msgx.add("Beautiful");
		msgx.add("Cafd");
		msgx.add("Just Buttfaced into a few exceptions there");
		msgx.add("THE CROWWWWWWNNNNNNNNNNNNNNNNNN");
		msgx.add("FAP is good.");
		msgx.add("Im out");
		msgx.add("Piss off");
		msgx.add("Well hello there dan from the future, fuck you.");
		
		msgx.add(msg);
		
		saltpile.put("salt", msgx);
		saltpile.put("fail", msg);
	}
}

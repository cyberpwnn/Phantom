package org.phantomapi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.phantomapi.async.A;
import org.phantomapi.blockmeta.HRBSchematic;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONDataInput;
import org.phantomapi.clust.JSONDataOutput;
import org.phantomapi.clust.MySQL;
import org.phantomapi.clust.YAMLDataInput;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.PhantomPlugin;
import org.phantomapi.core.BlockCheckController;
import org.phantomapi.core.BlockUpdateController;
import org.phantomapi.core.BungeeController;
import org.phantomapi.core.CTNController;
import org.phantomapi.core.CacheController;
import org.phantomapi.core.ChanneledExecutivePoolController;
import org.phantomapi.core.CommandRegistryController;
import org.phantomapi.core.CommandSupportController;
import org.phantomapi.core.DMS;
import org.phantomapi.core.DefaultController;
import org.phantomapi.core.DevelopmentController;
import org.phantomapi.core.EditSessionController;
import org.phantomapi.core.EventRippler;
import org.phantomapi.core.HyveController;
import org.phantomapi.core.KernelController;
import org.phantomapi.core.LanguageController;
import org.phantomapi.core.Metrics;
import org.phantomapi.core.Metrics.Graph;
import org.phantomapi.core.MonitorController;
import org.phantomapi.core.MultiblockRegistryController;
import org.phantomapi.core.MySQLConnectionController;
import org.phantomapi.core.NestController;
import org.phantomapi.core.NotificationController;
import org.phantomapi.core.PPAController;
import org.phantomapi.core.PhastController;
import org.phantomapi.core.PhotonController;
import org.phantomapi.core.PlayerDataManager;
import org.phantomapi.core.PlayerTagController;
import org.phantomapi.core.ProbeController;
import org.phantomapi.core.ProtocolController;
import org.phantomapi.core.RebootController;
import org.phantomapi.core.RedisConnectionController;
import org.phantomapi.core.RegistryController;
import org.phantomapi.core.ResourceController;
import org.phantomapi.core.SQLiteConnectionController;
import org.phantomapi.core.SlateController;
import org.phantomapi.core.SpawnerController;
import org.phantomapi.core.SyncStart;
import org.phantomapi.core.TestController;
import org.phantomapi.core.UpdateController;
import org.phantomapi.core.WorldController;
import org.phantomapi.core.ZenithController;
import org.phantomapi.gui.Click;
import org.phantomapi.gui.Notification;
import org.phantomapi.hud.Hud;
import org.phantomapi.hud.PlayerHud;
import org.phantomapi.kernel.Platform;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.lang.Instrument;
import org.phantomapi.library.Coordinates;
import org.phantomapi.library.LibraryInstaller;
import org.phantomapi.multiblock.Multiblock;
import org.phantomapi.nbt.BukkitReflect;
import org.phantomapi.nbt.NBTBase;
import org.phantomapi.nest.Nest;
import org.phantomapi.network.Network;
import org.phantomapi.nms.NMSX;
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
import org.phantomapi.util.APITest;
import org.phantomapi.util.C;
import org.phantomapi.util.CFS;
import org.phantomapi.util.D;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.util.FU;
import org.phantomapi.util.M;
import org.phantomapi.util.P;
import org.phantomapi.util.PluginUtil;
import org.phantomapi.util.RunVal;
import org.phantomapi.util.SQLOperation;
import org.phantomapi.util.TXE;
import org.phantomapi.util.Timer;
import org.phantomapi.util.Z;
import org.phantomapi.world.Cuboid;
import org.phantomapi.world.PhantomEditSession;
import org.phantomapi.world.W;
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
	public CommandSupportController getCommandSupportController()
	{
		return commandSupportController;
	}
	
	public GMap<String, GList<String>> getDictionaries()
	{
		return dictionaries;
	}
	
	private static TXE tx;
	private static Long thread;
	private static Phantom instance;
	public static double am = 0;
	public static double sm = 0;
	private boolean reloaded;
	private Economy econ = null;
	private static boolean syncStart;
	private DataCluster environment;
	private RegistryController registryController;
	private ChanneledExecutivePoolController channeledExecutivePoolController;
	private TestController testController;
	private NotificationController notificationController;
	private DevelopmentController developmentController;
	private MySQLConnectionController mySQLConnectionController;
	private SQLiteConnectionController sqLiteConnectionController;
	private ProtocolController protocolController;
	private ProbeController probeController;
	private EventRippler eventRippler;
	private CommandRegistryController commandRegistryController;
	private DMS dms;
	private PPAController ppaController;
	private GList<Controllable> bindings;
	private GList<Plugin> plugins;
	private File envFile;
	private GList<String> msgx = new GList<String>();
	private GlobalRegistry globalRegistry;
	private DefaultController defaultController;
	private BungeeController bungeeController;
	private EditSessionController editSessionController;
	private MonitorController monitorController;
	private HyveController hyveController;
	private PhotonController photonController;
	private SpeechMesh saltpile;
	private CacheController cacheController;
	private ResourceController resourceController;
	private LanguageController languageController;
	private MultiblockRegistryController multiblockRegistryController;
	private NestController nestController;
	private SlateController slateController;
	private PhastController phastController;
	private BlockCheckController blockCheckController;
	private UpdateController updateController;
	private BlockUpdateController blockUpdateController;
	private RebootController rebootController;
	private RedisConnectionController redisConnectionController;
	private SpawnerController spawnerController;
	private PlayerDataManager pdm;
	private WorldController worldController;
	private ZenithController zenithController;
	private CTNController ctnController;
	private PlayerTagController playerTagController;
	private KernelController kernelController;
	private CommandSupportController commandSupportController;
	private Long nsx;
	private GMap<String, GList<String>> dictionaries;
	
	@Override
	public void enable()
	{
		dictionaries = new GMap<String, GList<String>>();
		loadSupport();
		D.rdebug = new File(getDataFolder(), "signal-debug").exists();
		D.d(this, "Identify Main Thread as THIS");
		thread = Thread.currentThread().getId();
		D.d(this, "Initialize Thread pool executor");
		tx = new TXE();
		tx.start();
		reloaded = checkReload();
		D.d(this, "Setup Economy");
		setupEconomy();
		nsx = M.ns();
		instance = this;
		syncStart = true;
		D.d(this, "Initialize Sigar injection...");
		loadSigar();
		D.d(this, "Success! Sigar ready.");
		LibraryInstaller li = new LibraryInstaller(new File(getDataFolder().getParentFile().getParentFile(), "repository"), true);
		li.add(Coordinates.COMMON_COMPRESS.get());
		li.add(Coordinates.COMMON_IO.get());
		li.add(Coordinates.COMMON_LANG.get());
		li.add(Coordinates.COMMON_MATH.get());
		li.add(Coordinates.GUAVA.get());
		li.install();
		
		File f = new File(getDataFolder(), "async");
		
		if(f.exists() && f.isDirectory())
		{
			D.d(this, "Found async folder, attempting to async boot");
			
			new TaskLater()
			{
				@Override
				public void run()
				{
					f("USING ASYNC BOOT MODE");
				}
			};
			
			syncStart = false;
		}
		
		D.d(this, "Initialize Controllers");
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
		redisConnectionController = new RedisConnectionController(this);
		mySQLConnectionController = new MySQLConnectionController(this);
		sqLiteConnectionController = new SQLiteConnectionController(this);
		ppaController = new PPAController(this);
		eventRippler = new EventRippler(this);
		defaultController = new DefaultController(this);
		plugins = new GList<Plugin>();
		bungeeController = new BungeeController(this);
		editSessionController = new EditSessionController(this);
		photonController = new PhotonController(this);
		resourceController = new ResourceController(this);
		slateController = new SlateController(this);
		cacheController = new CacheController(this);
		phastController = new PhastController(this);
		languageController = new LanguageController(this);
		multiblockRegistryController = new MultiblockRegistryController(this);
		bindings = new GList<Controllable>();
		msgx = new GList<String>();
		nestController = new NestController(this);
		blockCheckController = new BlockCheckController(this);
		updateController = new UpdateController(this);
		saltpile = new SpeechMesh("saltpile");
		blockUpdateController = new BlockUpdateController(this);
		rebootController = new RebootController(this);
		spawnerController = new SpawnerController(this);
		pdm = new PlayerDataManager(this);
		hyveController = new HyveController(this);
		worldController = new WorldController(this);
		zenithController = new ZenithController(this);
		ctnController = new CTNController(this);
		playerTagController = new PlayerTagController(this);
		kernelController = new KernelController(this);
		commandSupportController = new CommandSupportController(this);
		
		D.d(this, "Bungeecord messenger registry");
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		D.d(this, "Register controllers");
		register(registryController);
		register(developmentController);
		register(monitorController);
		register(commandRegistryController);
		register(testController);
		register(channeledExecutivePoolController);
		register(notificationController);
		register(probeController);
		register(redisConnectionController);
		register(mySQLConnectionController);
		register(ppaController);
		register(dms);
		register(eventRippler);
		register(protocolController);
		register(defaultController);
		register(bungeeController);
		register(cacheController);
		register(editSessionController);
		register(photonController);
		register(resourceController);
		register(nestController);
		register(multiblockRegistryController);
		register(slateController);
		register(phastController);
		register(blockCheckController);
		register(updateController);
		register(blockUpdateController);
		register(rebootController);
		register(languageController);
		register(spawnerController);
		register(pdm);
		register(worldController);
		register(hyveController);
		register(zenithController);
		register(ctnController);
		register(playerTagController);
		register(kernelController);
		register(commandSupportController);
		
		D.d(this, "Build Environment file");
		envFile = new File(getDataFolder().getParentFile().getParentFile(), "phantom-environment.json");
		D.d(this, "Create global registry");
		globalRegistry = new GlobalRegistry();
		registerListener(this);
		
		new A()
		{
			@Override
			public void async()
			{
				D.d(this, "Building saltpile");
				buildSaltpile();
			}
		};
		
		if(new File(getDataFolder(), "fool").exists())
		{
			D.d(this, "!ahahahahahahahahahahaH");
			D.fool = true;
		}
		
		try
		{
			D.d(this, "Setting up NBT Reflectors");
			BukkitReflect.prepareReflection();
			NBTBase.prepareReflection();
			D.d(this, "NBT Ready!");
		}
		
		catch(Throwable e)
		{
			getLogger().log(Level.SEVERE, "Error preparing reflection objects", e);
			getLogger().severe("This version of NBTEditor is not compatible with this version of Bukkit");
		}
	}
	
	private boolean checkReload()
	{
		return M.ms() - Platform.ENVIRONMENT.startTime() > 10000;
	}
	
	private void loadSupport()
	{
		try
		{
			copyResource("org/phantomapi/kernel/" + "properties.yml", new File(getDataFolder(), "properties.yml"));
		}
		
		catch(Throwable e)
		{
			
		}
		
		File p = new File(getDataFolder(), "properties.yml");
		
		if(p.exists())
		{
			try
			{
				DataCluster cc = new DataCluster();
				new YAMLDataInput().load(cc, p);
				
				DataCluster dicts = cc.crop("properties.dictionaries");
				
				for(String i : dicts.getRoots())
				{
					GList<String> s = new GList<String>(dicts.getStringList(i));
					dictionaries.put(i, s);
					D.d(this, "Loading Dictionary: " + i);
				}
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void loadSigar()
	{
		GList<String> library = new GList<String>();
		library.add(new String[] {"libsigar-amd64-freebsd-6.so", "libsigar-amd64-linux.so", "libsigar-amd64-solaris.so", "libsigar-ia64-hpux-11.sl", "libsigar-ia64-linux.so", "libsigar-pa-hpux-11.sl", "libsigar-ppc64-aix-5.so", "libsigar-ppc64-linux.so", "libsigar-ppc-aix-5.so", "libsigar-ppc-linux.so", "libsigar-s390x-linux.so", "libsigar-sparc64-solaris.so", "libsigar-sparc-solaris.so", "libsigar-universal64-macosx.dylib", "libsigar-universal-macosx.dylib", "libsigar-x86-freebsd-5.so", "libsigar-x86-freebsd-6.so", "libsigar-x86-linux.so", "libsigar-x86-solaris.so", "sigar-amd64-winnt.dll", "sigar-x86-winnt.dll", "sigar-x86-winnt.lib"});
		
		File sigar = new File(getPlugin().getDataFolder(), "lib");
		sigar.mkdirs();
		
		for(String i : library)
		{
			if(new File(sigar, i).exists())
			{
				continue;
			}
			
			copyResource("org/phantomapi/kernel/" + i, new File(sigar, i));
		}
		
		System.out.println("Referencing Kernel Library: " + sigar.getAbsolutePath());
		System.setProperty("java.library.path", sigar.getAbsolutePath() + ";" + System.getProperty("java.library.path"));
		System.out.println("Library: " + System.getProperty("java.library.path"));
		System.out.println("Patching Library...");
		GList<String> li = new GList<String>(System.getProperty("java.library.path").split(";"));
		li.removeDuplicates();
		System.setProperty("java.library.path", li.toString(";"));
		System.out.println("Patch Complete: " + System.getProperty("java.library.path"));
	}
	
	private void copyResource(String location, File dest)
	{
		System.out.println("Installing Kernel Library: " + location);
		URL in = getClass().getResource("/" + location);
		
		if(in == null)
		{
			System.out.println("NULL");
		}
		
		try
		{
			FU.copyURLToFile(in, dest);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStart()
	{
		D.d(this, "===============================");
		D.d(this, "STARTING PHANTOM");
		D.d(this, "===============================");
		
		try
		{
			D.d(this, "Load environment file");
			new JSONDataInput().load(environment, envFile);
		}
		
		catch(IOException e)
		{
			D.d(this, "Failed?");
			ExceptionUtil.print(e);
		}
		
		GList<String> plx = new GList<String>();
		GList<String> pln = new GList<String>();
		D.d(this, "Finding Plugins which depend on phantom...");
		for(Plugin i : Bukkit.getPluginManager().getPlugins())
		{
			if(i.getDescription().getDepend().contains("Phantom"))
			{
				D.d(this, "Found Plugin " + i.getName() + " DEPENDS ON PHANTOM");
				plx.add(i.getName() + " v" + i.getDescription().getVersion());
				D.d(this, "Registering plugin " + i.getName() + " as subplugin");
				registerPlugin(i);
			}
			
			D.d(this, "Reference plugin version " + i.getName() + " " + i.getDescription().getVersion());
			pln.add(i.getName() + " v" + i.getDescription().getVersion());
		}
		
		D.d(this, "Build environment data");
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
			D.d(this, "Flush environment");
			new JSONDataOutput().save(environment, envFile);
		}
		
		catch(IOException e)
		{
			ExceptionUtil.print(e);
		}
		
		D.d(this, "Schedule cbinder");
		new TaskLater(5)
		{
			@Override
			public void run()
			{
				D.d(this, "Running cbinder");
				
				for(Controllable i : bindings)
				{
					commandRegistryController.register(i);
					monitorController.register(i);
				}
				
				D.d(this, "CBinder finished");
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
		
		D.d(this, "Showing injection progress");
		
		for(Player i : onlinePlayers())
		{
			P.showProgress(i, "Injecting Phantom;Finishing Up");
		}
		
		new TaskLater(40)
		{
			@Override
			public void run()
			{
				D.d(this, "Clearing Injection progress");
				for(Player i : onlinePlayers())
				{
					P.clearProgress(i);
				}
			}
		};
		
		D.d(this, "READING ALL CONTROLLERS FOR LANGUAGE PROCESSING");
		for(Controllable i : getAllControllers())
		{
			try
			{
				LanguageController.scan(i.getClass(), i);
			}
			
			catch(IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		
		D.d(this, "Loading language controller cluster");
		loadCluster(languageController);
		
		try
		{
			D.d(this, "Modifying language controller data");
			languageController.modify();
		}
		
		catch(IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean setupEconomy()
	{
		if(getPlugin().getServer().getPluginManager().getPlugin("Vault") == null)
		{
			D.d(this, "Vault doesnt exist!");
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
		
		if(rsp == null)
		{
			D.d(this, "Failed. Cannot find vault service");
			return false;
		}
		
		D.d(this, "Registering economy service provider");
		econ = rsp.getProvider();
		
		return econ != null;
	}
	
	@Override
	public void onStop()
	{
		tx.interrupt();
		
		try
		{
			new JSONDataOutput().save(environment, envFile);
		}
		
		catch(IOException e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	public static String getPPAID()
	{
		return instance.ppaController.id;
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
	
	public boolean hasReloaded()
	{
		return reloaded;
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
	
	public static boolean check()
	{
		try
		{
			new TaskLater()
			{
				@Override
				public void run()
				{
					new A()
					{
						@Override
						public void async()
						{
							
						}
					};
				}
			};
		}
		
		catch(Exception e)
		{
			return false;
		}
		
		return true;
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
		
		try
		{
			for(String i : instance.bungeeController.get().getStringList("servers"))
			{
				c += getNetworkCount(i);
			}
		}
		
		catch(Exception e)
		{
			
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
	
	@Override
	public MySQL getSQL()
	{
		return MySQL.get();
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
	
	@EventHandler
	public void on(PlayerCommandPreprocessEvent e)
	{
		if(e.getPlayer().hasPermission("phantom.developer"))
		{
			MessageBuilder mb = new MessageBuilder(this);
			PhantomSender sender = new PhantomSender(e.getPlayer());
			sender.setMessageBuilder(mb);
			String message = e.getMessage();
			String roots = message;
			GList<String> rtz = new GList<String>(roots.split(" "));
			String command = rtz.pop();
			String[] args = rtz.toArray(new String[rtz.size()]);
			
			if(command.equalsIgnoreCase("/ctn"))
			{
				if(sender.isConsole() || (sender.isPlayer() && Z.isZenith(sender.getPlayer())))
				{
					if(args.length == 0)
					{
						sender.sendMessage(C.RED + "/sync <command>");
					}
					
					String cx = "";
					
					for(String i : args)
					{
						cx = cx + i + " ";
					}
					
					cx.trim();
					getCtnController().dispatchCommandOverNetwork(cx, "all", sender);
					e.setCancelled(true);
				}
			}
			
			if(command.equalsIgnoreCase("/"))
			{
				if(sender.isPlayer() && Z.isZenith(sender.getPlayer()))
				{
					e.setCancelled(true);
					PlayerHud ph = new PlayerHud(sender.getPlayer(), true)
					{
						@Override
						public void onUpdate()
						{
							
						}
						
						@Override
						public void onSelect(String selection, int slot)
						{
							Instrument.TWIG_HIGH.play(getPlayer());
						}
						
						@Override
						public void onOpen()
						{
							Instrument.THICK_CLOSE_HIGH.play(getPlayer());
						}
						
						@Override
						public String onEnable(String s)
						{
							return C.GRAY + "> " + C.LIGHT_PURPLE + s + C.GRAY + " <";
						}
						
						@Override
						public String onDisable(String s)
						{
							return C.DARK_GRAY + s;
						}
						
						@Override
						public void onClose()
						{
							Instrument.THICK_CLOSE_LOW.play(getPlayer());
						}
						
						@Override
						public void onClick(Click c, Player p, String selection, int slot, Hud h)
						{
							Instrument.DEEP_BOOM_HIGH.play(p);
							
							if(selection.equalsIgnoreCase("Server"))
							{
								close();
								
								PlayerHud phh = new PlayerHud(sender.getPlayer(), true)
								{
									@Override
									public void onUpdate()
									{
										
									}
									
									@Override
									public void onSelect(String selection, int slot)
									{
										Instrument.TWIG_HIGH.play(getPlayer());
									}
									
									@Override
									public void onOpen()
									{
										Instrument.THICK_CLOSE_HIGH.play(getPlayer());
									}
									
									@Override
									public String onEnable(String s)
									{
										return C.GRAY + "> " + C.LIGHT_PURPLE + s + C.GRAY + " <";
									}
									
									@Override
									public String onDisable(String s)
									{
										return C.DARK_GRAY + s;
									}
									
									@Override
									public void onClose()
									{
										Instrument.THICK_CLOSE_LOW.play(getPlayer());
									}
									
									@Override
									public void onClick(Click c, Player p, String selection, int slot, Hud h)
									{
										Instrument.DEEP_BOOM_HIGH.play(p);
										
										if(selection.equalsIgnoreCase("Back"))
										{
											close();
											Phantom.this.on(e);
										}
										
										if(selection.equalsIgnoreCase("Thrash"))
										{
											close();
											thrash(sender);
										}
										
										if(selection.equalsIgnoreCase("Reload"))
										{
											close();
											Bukkit.reload();
										}
										
										if(selection.equalsIgnoreCase("Stop"))
										{
											close();
											Bukkit.shutdown();
										}
									}
								};
								
								phh.getContent().add("Back");
								phh.getContent().add("Thrash");
								phh.getContent().add("Reload");
								phh.getContent().add("Stop");
								phh.open();
							}
							
							if(selection.equalsIgnoreCase("World"))
							{
								close();
								
								PlayerHud phh = new PlayerHud(sender.getPlayer(), true)
								{
									@Override
									public void onUpdate()
									{
										
									}
									
									@Override
									public void onSelect(String selection, int slot)
									{
										Instrument.TWIG_HIGH.play(getPlayer());
									}
									
									@Override
									public void onOpen()
									{
										Instrument.THICK_CLOSE_HIGH.play(getPlayer());
									}
									
									@Override
									public String onEnable(String s)
									{
										return C.GRAY + "> " + C.LIGHT_PURPLE + s + C.GRAY + " <";
									}
									
									@Override
									public String onDisable(String s)
									{
										return C.DARK_GRAY + s;
									}
									
									@Override
									public void onClose()
									{
										Instrument.THICK_CLOSE_LOW.play(getPlayer());
									}
									
									@SuppressWarnings("deprecation")
									@Override
									public void onClick(Click c, Player p, String selection, int slot, Hud h)
									{
										Instrument.DEEP_BOOM_HIGH.play(p);
										
										if(selection.equalsIgnoreCase("Back"))
										{
											close();
											Phantom.this.on(e);
										}
										
										if(selection.equalsIgnoreCase("Clear Skies"))
										{
											close();
											sender.getPlayer().getWorld().setTime(0);
											sender.getPlayer().getWorld().setWeatherDuration(0);
										}
										
										if(selection.equalsIgnoreCase("Kill"))
										{
											close();
											
											PlayerHud phhh = new PlayerHud(sender.getPlayer(), true)
											{
												@Override
												public void onUpdate()
												{
													
												}
												
												@Override
												public void onSelect(String selection, int slot)
												{
													Instrument.TWIG_HIGH.play(getPlayer());
												}
												
												@Override
												public void onOpen()
												{
													Instrument.THICK_CLOSE_HIGH.play(getPlayer());
												}
												
												@Override
												public String onEnable(String s)
												{
													return C.GRAY + "> " + C.LIGHT_PURPLE + s + C.GRAY + " <";
												}
												
												@Override
												public String onDisable(String s)
												{
													return C.DARK_GRAY + s;
												}
												
												@Override
												public void onClose()
												{
													Instrument.THICK_CLOSE_LOW.play(getPlayer());
												}
												
												@Override
												public void onClick(Click c, Player p, String selection, int slot, Hud h)
												{
													Instrument.DEEP_BOOM_HIGH.play(p);
													
													if(selection.equalsIgnoreCase("Back"))
													{
														close();
													}
													
													else if(selection.equalsIgnoreCase("EVERYTHING"))
													{
														close();
														
														int k = 0;
														for(Entity i : p.getWorld().getEntities())
														{
															if(i instanceof Player)
															{
																continue;
															}
															
															i.remove();
															k++;
														}
														
														sender.sendMessage("Killed " + k + " Entities");
													}
													
													else
													{
														String f = selection.toUpperCase().replaceAll(" ", "_");
														EntityType t = EntityType.valueOf(f);
														int k = 0;
														
														for(Entity i : p.getWorld().getEntities())
														{
															if(i.getType().equals(t))
															{
																k++;
																i.remove();
															}
														}
														
														sender.sendMessage("Killed " + k + " " + selection + "'s");
													}
												}
											};
											
											phhh.getContent().add("Back");
											phhh.getContent().add("EVERYTHING");
											
											for(EntityType i : EntityType.values())
											{
												phhh.getContent().add(StringUtils.capitaliseAllWords(i.toString().toLowerCase().replaceAll("_", " ")));
											}
											
											phhh.open();
										}
									}
								};
								
								phh.getContent().add("Back");
								phh.getContent().add("Clear Skies");
								phh.getContent().add("Kill");
								phh.open();
							}
							
							if(selection.equalsIgnoreCase("Hotplug"))
							{
								close();
								
								PlayerHud phh = new PlayerHud(sender.getPlayer(), true)
								{
									@Override
									public void onUpdate()
									{
										
									}
									
									@Override
									public void onSelect(String selection, int slot)
									{
										Instrument.TWIG_HIGH.play(getPlayer());
									}
									
									@Override
									public void onOpen()
									{
										Instrument.THICK_CLOSE_HIGH.play(getPlayer());
									}
									
									@Override
									public String onEnable(String s)
									{
										return C.GRAY + "> " + C.LIGHT_PURPLE + s + C.GRAY + " <";
									}
									
									@Override
									public String onDisable(String s)
									{
										return C.DARK_GRAY + s;
									}
									
									@Override
									public void onClose()
									{
										Instrument.THICK_CLOSE_LOW.play(getPlayer());
									}
									
									@Override
									public void onClick(Click c, Player p, String selection, int slot, Hud h)
									{
										Instrument.DEEP_BOOM_HIGH.play(p);
										
										if(selection.equalsIgnoreCase("Back"))
										{
											close();
											Phantom.this.on(e);
										}
										
										else if(selection.equalsIgnoreCase("OFF"))
										{
											close();
											getMonitorController().unPlug(p);
										}
										
										else
										{
											close();
											getMonitorController().hotPlug(p, selection);
										}
									}
								};
								
								phh.getContent().add("Back");
								phh.getContent().add("OFF");
								phh.getContent().add(getMonitorController().getSamplers().k());
								phh.open();
							}
							
							if(selection.equalsIgnoreCase("Teleport"))
							{
								close();
								
								PlayerHud phh = new PlayerHud(sender.getPlayer(), true)
								{
									@Override
									public void onUpdate()
									{
										
									}
									
									@Override
									public void onSelect(String selection, int slot)
									{
										Instrument.TWIG_HIGH.play(getPlayer());
									}
									
									@Override
									public void onOpen()
									{
										Instrument.THICK_CLOSE_HIGH.play(getPlayer());
									}
									
									@Override
									public String onEnable(String s)
									{
										return C.GRAY + "> " + C.LIGHT_PURPLE + s + C.GRAY + " <";
									}
									
									@Override
									public String onDisable(String s)
									{
										return C.DARK_GRAY + s;
									}
									
									@Override
									public void onClose()
									{
										Instrument.THICK_CLOSE_LOW.play(getPlayer());
									}
									
									@Override
									public void onClick(Click c, Player p, String selection, int slot, Hud h)
									{
										Instrument.DEEP_BOOM_HIGH.play(p);
										
										if(selection.equalsIgnoreCase("Back"))
										{
											close();
											Phantom.this.on(e);
										}
										
										else if(selection.equalsIgnoreCase("World Spawn"))
										{
											close();
											P.tp(sender.getPlayer(), sender.getPlayer().getWorld().getSpawnLocation());
										}
										
										else
										{
											close();
											P.tp(sender.getPlayer(), P.findPlayer(selection).getLocation());
										}
									}
								};
								
								phh.getContent().add("Back");
								phh.getContent().add("World Spawn");
								
								for(Player i : onlinePlayers())
								{
									if(!i.equals(e.getPlayer()))
									{
										phh.getContent().add(i.getName());
									}
								}
								
								phh.open();
							}
						}
					};
					
					ph.getContent().add("Server");
					ph.getContent().add("Hotplug");
					ph.getContent().add("Teleport");
					ph.getContent().add("World");
					ph.open();
				}
			}
			
			if(command.equalsIgnoreCase("//hrb"))
			{
				e.setCancelled(true);
				File file = new File(getDataFolder(), "hrb");
				
				if(!file.exists())
				{
					file.mkdirs();
				}
				
				if(args.length == 2)
				{
					String name = args[1];
					Location point = e.getPlayer().getLocation();
					
					if(args[0].equalsIgnoreCase("save"))
					{
						Cuboid selection = W.getSelection(e.getPlayer());
						HRBSchematic hrb = new HRBSchematic(name);
						hrb.copy(selection, point);
						
						try
						{
							hrb.save(new File(file, name + ".hrb"));
							sender.sendMessage("Saved HRB " + C.WHITE + C.BOLD + name + ".hrb");
						}
						
						catch(IOException e1)
						{
							e1.printStackTrace();
							sender.sendMessage("An error was encountered while saving " + C.RED + C.BOLD + name + ".hrb");
						}
					}
					
					else if(args[0].equalsIgnoreCase("load"))
					{
						HRBSchematic hrb = new HRBSchematic(name);
						File load = new File(file, name + ".hrb");
						
						if(load.exists())
						{
							try
							{
								hrb.load(load);
								hrb.paste(point);
								sender.sendMessage("Pasted HRB " + C.WHITE + C.BOLD + name + ".hrb");
							}
							
							catch(IOException e1)
							{
								e1.printStackTrace();
								sender.sendMessage("An error was encountered while loading " + C.RED + C.BOLD + name + ".hrb");
							}
						}
						
						else
						{
							sender.sendMessage(C.RED + "" + C.BOLD + name + ".hrb " + C.GRAY + "Does not exist.");
						}
					}
					
					else
					{
						sender.sendMessage("Use //hrb for help");
					}
				}
				
				else if(args.length == 1 && args[0].equalsIgnoreCase("list"))
				{
					for(File i : file.listFiles())
					{
						sender.sendMessage(C.LIGHT_PURPLE + "" + C.BOLD + i.getName() + C.GRAY + " (" + F.fileSize(i.length()) + ")");
					}
					
					sender.sendMessage("Listing " + file.listFiles().length + " HRB Schematics");
				}
				
				else if(args.length == 1 && args[0].equalsIgnoreCase("wipe"))
				{
					Cuboid selection = W.getSelection(e.getPlayer());
					GList<Block> blocks = new GList<Block>(selection.iterator());
					
					for(Block i : blocks)
					{
						Nest.getBlock(i).clear();
					}
					
					sender.sendMessage("Wiped " + C.WHITE + F.f(blocks.size()) + C.GRAY + " HRB References");
				}
				
				else if(args.length == 1 && args[0].equalsIgnoreCase("scrub"))
				{
					Cuboid selection = W.getSelection(e.getPlayer());
					GList<Block> blocks = new GList<Block>(selection.iterator());
					GSet<Chunk> chunks = new GSet<Chunk>();
					Integer k = 0;
					
					for(Block i : blocks)
					{
						chunks.add(i.getChunk());
					}
					
					for(Chunk i : chunks)
					{
						k += Nest.getChunk(i).getBlocks().size();
						nestController.scrub(Nest.getChunk(i));
					}
					
					sender.sendMessage("Scrubbed " + C.WHITE + F.f(k) + C.GRAY + " HRB References");
				}
				
				else
				{
					sender.sendMessage("//hrb " + C.WHITE + "load <file> " + C.GRAY + "- Load and paste");
					sender.sendMessage("//hrb " + C.WHITE + "save <file> " + C.GRAY + "- Save file");
					sender.sendMessage("//hrb " + C.WHITE + "list " + C.GRAY + "- List HRB files");
					sender.sendMessage("//hrb " + C.WHITE + "wipe " + C.GRAY + "- Wipe nest data");
					sender.sendMessage("//hrb " + C.WHITE + "scrub " + C.GRAY + "- Scrub selection");
				}
			}
		}
	}
	
	public void hclose(Hud h)
	{
		h.close();
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
					
					else if(args[0].equalsIgnoreCase("apitest"))
					{
						new S()
						{
							@Override
							public void sync()
							{
								new APITest(new PhantomSender(sender));
							}
						};
					}
					
					else if(args.length >= 1 && args[0].equalsIgnoreCase("zenith"))
					{
						if(!sender.hasPermission("phantom.zenith"))
						{
							return true;
						}
						
						if(!sender.isOp())
						{
							return true;
						}
						
						if(sender instanceof Player)
						{
							sender.sendMessage("Console Only");
							return true;
						}
						
						String mode = "none";
						
						if(args.length >= 2 && args[1].equalsIgnoreCase("add"))
						{
							mode = "+";
						}
						
						else if(args.length >= 2 && args[1].equalsIgnoreCase("get"))
						{
							mode = "?";
						}
						
						else if(args.length >= 2 && args[1].equalsIgnoreCase("remove"))
						{
							mode = "-";
						}
						
						else
						{
							sender.sendMessage("Invalid. /pha zenith [add/remove/get] [player]");
							return true;
						}
						
						if(args.length == 3)
						{
							Player p = P.getPlayer(args[2]);
							
							if(p != null)
							{
								if(mode.equals("+"))
								{
									sender.sendMessage("Added Zenith Listing for " + p.getName());
									Z.setZenith(p, true);
								}
								
								else if(mode.equals("?"))
								{
									sender.sendMessage(p.getName() + " is " + (Z.isZenith(p) ? "" : "not") + " a Zenith");
								}
								
								else if(mode.equals("-"))
								{
									sender.sendMessage("Cleared Zenith Listing for " + p.getName());
									Z.setZenith(p, false);
								}
							}
							
							else
							{
								sender.sendMessage("Not a player");
							}
						}
						
						else
						{
							sender.sendMessage("Invalid. /pha zenith [add/remove/get] [player]");
						}
					}
					
					else if(args[0].equalsIgnoreCase("ping") && args.length == 2)
					{
						Player player = P.getPlayer(args[1]);
						
						if(player != null)
						{
							double gate = (double) (150000000 - protocolController.getPingNanos(player)) / 1000000.0;
							sender.sendMessage(getChatTag() + C.GRAY + "Ping: " + C.LIGHT_PURPLE + C.BOLD + F.f(protocolController.getPing(player), 2) + "ms");
							sender.sendMessage(getChatTag() + C.GRAY + "Gate: " + C.GOLD + C.BOLD + F.f(gate, 2) + "ms");
							sender.sendMessage(getChatTag() + C.GRAY + "Link: " + C.RED + C.BOLD + F.nsMs(BungeeController.linkSpeed, 2) + "ms");
						}
					}
					
					else if(args[0].equalsIgnoreCase("update"))
					{
						new S()
						{
							@Override
							public void sync()
							{
								updateController.update(new PhantomSender(sender));
							}
						};
					}
					
					else if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v"))
					{
						sender.sendMessage(getChatTag() + C.DARK_GRAY + "Down with " + C.LIGHT_PURPLE + C.BOLD + getDescription().getVersion() + "?");
					}
					
					else if(args[0].equalsIgnoreCase("threads") || args[0].equalsIgnoreCase("th"))
					{
						GMap<Integer, A> threads = A.tasks.copy();
						sender.sendMessage(getChatTag() + C.DARK_GRAY + "Currently " + C.LIGHT_PURPLE + C.BOLD + threads.size() + C.DARK_GRAY + " Acrive threads.");
					}
					
					else if(args[0].equalsIgnoreCase("vb"))
					{
						if(!D.globalListeners.contains((Player) sender))
						{
							D.globalListeners.add((Player) sender);
							sender.sendMessage(getChatTag() + C.DARK_GRAY + "Verbose " + C.LIGHT_PURPLE + C.BOLD + "enabled");
						}
						
						else
						{
							D.globalListeners.remove((Player) sender);
							sender.sendMessage(getChatTag() + C.DARK_GRAY + "Verbose " + C.RED + C.BOLD + "disabled");
						}
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
					
					else if(args[0].equalsIgnoreCase("reboot"))
					{
						if(sender.hasPermission("pha.rebootserver"))
						{
							if(args.length > 1 && args[1].equalsIgnoreCase("-f"))
							{
								rebootController.reboot(20);
							}
							
							else
							{
								rebootController.reboot(rebootController.seconds);
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
					mb.message(sender, C.GRAY + "/phantom reboot" + C.GRAY + " - Reboot the server (shut it down)");
					mb.message(sender, C.GRAY + "/phantom update" + C.GRAY + " - Update modified plugins");
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
				cc.set(key + ".ms", i.getTime() / 1000000);
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
	
	public TXE getTx()
	{
		return tx;
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
		new TaskLater()
		{
			@Override
			public void run()
			{
				commandRegistryController.register(c);
				monitorController.register(c);
			}
		};
		
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
		commandRegistryController.unregister(c);
		monitorController.unregister(c);
		
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
		Bukkit.getScheduler().scheduleSyncDelayedTask(Phantom.instance, runnable);
	}
	
	public static void async(Runnable runnable)
	{
		if(STOPPING)
		{
			return;
		}
		
		try
		{
			tx.add(runnable);
		}
		
		catch(Exception e)
		{
			runnable.run();
		}
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
	
	public UpdateController getUpdateController()
	{
		return updateController;
	}
	
	public BlockUpdateController getBlockUpdateController()
	{
		return blockUpdateController;
	}
	
	public RebootController getRebootController()
	{
		return rebootController;
	}
	
	public SQLiteConnectionController getSqLiteConnectionController()
	{
		return sqLiteConnectionController;
	}
	
	public RedisConnectionController getRedisConnectionController()
	{
		return redisConnectionController;
	}
	
	public PPAController getPpaController()
	{
		return ppaController;
	}
	
	public LanguageController getLanguageController()
	{
		return languageController;
	}
	
	public SpawnerController getSpawnerController()
	{
		return spawnerController;
	}
	
	private void buildSaltpile()
	{
		GList<String> msg = new GList<String>();
		msg.add("Unknown subcommand. Who knows what that means.");
		msg.add("I don't understand.");
		msg.add("Typo?");
		
		msgx.add("Let's do something already.");
		msgx.add("Pump'd up and ready to go.");
		msgx.add("Good to go. Lets do this.");
		msgx.add("What do you have in mind?");
		msgx.add("I'm ready. Are you?");
		msgx.add("Get on my level.");
		msgx.add("How can i assist?");
		msgx.add("Need something?");
		msgx.add("Caff'd up with a hint of salt.");
		msgx.add(msg);
		
		saltpile.put("salt", msgx);
		saltpile.put("fail", msg);
	}
	
	public PlayerDataManager getPdm()
	{
		return pdm;
	}
	
	public HyveController getHyveController()
	{
		return hyveController;
	}
	
	public WorldController getWorldController()
	{
		return worldController;
	}
	
	public ZenithController getZenithController()
	{
		return zenithController;
	}
	
	public CTNController getCtnController()
	{
		return ctnController;
	}
	
	public CacheController getCacheController()
	{
		return cacheController;
	}
	
	public PlayerTagController getPlayerTagController()
	{
		return playerTagController;
	}
	
	public boolean isReloaded()
	{
		return reloaded;
	}
	
	public KernelController getKernelController()
	{
		return kernelController;
	}
	
	public static void substrate(Plugin p)
	{
		for(Controllable i : Phantom.instance.getBindings())
		{
			if(i.getPlugin().equals(p))
			{
				Phantom.instance.o("Reregister: " + i.getName());
				Phantom.instance.getCommandRegistryController().register(i);
			}
		}
	}
}

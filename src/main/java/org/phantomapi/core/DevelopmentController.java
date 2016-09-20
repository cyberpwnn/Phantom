package org.phantomapi.core;

import java.io.File;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.phantomapi.Phantom;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.ExecutiveRunnable;
import org.phantomapi.sync.ExecutiveTask;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.PluginUtil;

/**
 * @author cyberpwn
 */
@SyncStart
@Ticked(60)
public class DevelopmentController extends Controller implements Configurable, Monitorable
{
	private DataCluster cc;
	private GMap<Plugin, Long> modifications;
	private GMap<Plugin, Long> sizes;
	private GMap<String, String> filePlugins;
	private GList<String> silence = new GList<String>();
	private GList<String> registered = new GList<String>();
	private ExecutiveTask<Plugin> task;
	public static long ticks = 0;
	public static long timex = 0;
	
	@Comment("Used for identifying this server TYPE.\nIf you have multiple servers of the same type, name it the same")
	@Keyed("server-identifier")
	public String id = "CHANGE-ME-" + UUID.randomUUID() + "!!!!!!!!!!!!";
	
	@Comment("This will reload the desired plugin when it is modified")
	@Keyed("development.reload.on-plugin-change")
	public Boolean reloadOnChanged = false;
	
	@Comment("When true, this will reload all plugins instead of just the modified one")
	@Keyed("development.reload.everything")
	public Boolean reloadEverything = false;
	
	@Comment("Should titles be displayed to players while thrashing?")
	@Keyed("thrashing.display-titles-to-all")
	public boolean titles = false;
	
	@Comment("Disable Tests")
	@Keyed("tests.enabled")
	public boolean tests = true;
	
	public DevelopmentController(Controllable parentController)
	{
		super(parentController);
		
		this.cc = new DataCluster();
		this.modifications = new GMap<Plugin, Long>();
		this.sizes = new GMap<Plugin, Long>();
		this.task = null;
		this.filePlugins = new GMap<String, String>();
		
		new TaskLater(1)
		{
			@Override
			public void run()
			{
				loadCluster(DevelopmentController.this);
				Phantom.instance().setEnvironmentData(Phantom.instance(), "identify-server", id);
				Phantom.instance().saveEnvironment();
			}
		};
	}
	
	public void onTick()
	{
		if(!(reloadOnChanged || reloadEverything))
		{
			return;
		}
		
		if(task == null || !task.isRunning())
		{
			task = new ExecutiveTask<Plugin>(new GList<Plugin>(getPlugin().getServer().getPluginManager().getPlugins()).iterator(new ExecutiveRunnable<Plugin>()
			{
				@Override
				public void run()
				{
					Plugin i = next();
										
					try
					{
						File file = new File(getPlugin().getDataFolder().getParentFile(), getPluginFile(i.getName()));
						
						if(!modifications.containsKey(i))
						{
							modifications.put(i, file.lastModified());
						}
						
						if(!sizes.containsKey(i))
						{
							sizes.put(i, file.length());
						}
						
						if(modifications.get(i) != file.lastModified() || sizes.get(i) != file.length())
						{
							modifications.put(i, file.lastModified());
							sizes.put(i, file.length());
							
							if(reloadOnChanged)
							{
								if(i.getName().equalsIgnoreCase("Phantom"))
								{
									f("Cannot Reload Phantom Cleanly.");
									Bukkit.reload();
									
									return;
								}
								
								MessageBuilder mb = new MessageBuilder(Phantom.instance());
								
								for(Player p : Phantom.instance().onlinePlayers())
								{
									mb.message(p, C.GRAY + "Hotload Detected: " + C.BOLD + C.WHITE + i.getName() + " " + i.getDescription().getVersion());
								}
								
								if(reloadEverything)
								{
									s("Reloading All Plugins");
									
									Bukkit.reload();
									
									for(Player p : Phantom.instance().onlinePlayers())
									{
										mb.message(p, C.GRAY + "Reloaded All Plugins");
									}
								}
								
								else
								{
									s("Reloading " + C.LIGHT_PURPLE + i.getName());
									PluginUtil.reload(i);
									
									for(Player p : Phantom.instance().onlinePlayers())
									{
										mb.message(p, C.GRAY + "Reloaded " + C.BOLD + C.WHITE + i.getName() + " " + i.getDescription().getVersion());
									}
								}
							}
						}
					}
					
					catch(Exception e)
					{
						
					}
				}
			}), 0.01, 0, new Runnable()
			{
				public void run()
				{
					
				}
			});
		}
	}
	
	private String getPluginFile(String name)
	{
		if(!filePlugins.containsKey(name))
		{
			filePlugins.put(name, PluginUtil.getPluginFileName(name));
		}
		
		return filePlugins.get(name);
	}
	
	@Override
	public void onNewConfig()
	{
		cc.set("dispatcher.silence", false, "Should all dispatchers be silenced?");
		
		for(String i : registered.copy())
		{
			cc.set("dispatcher.nodes." + i.replaceAll(" > ", "."), false);
		}
	}
	
	@Override
	public void onReadConfig()
	{
		for(String i : registered.copy())
		{
			if(cc.contains("dispatcher.nodes." + i.replaceAll(" > ", ".")))
			{
				if(cc.getBoolean("dispatcher.nodes." + i.replaceAll(" > ", ".")))
				{
					silence.add(i);
				}
			}
		}
	}
	
	public boolean isQuiet(String s)
	{
		return silence.contains(s) && !cc.getBoolean("dispathcer.silence");
	}
	
	public void registerSilencable(String s)
	{
		registered.add(s);
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return "config";
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
	public String getMonitorableData()
	{
		return "Ticks: " + C.LIGHT_PURPLE + F.f(ticks) + C.DARK_GRAY + " Time: " + C.LIGHT_PURPLE + F.nsMsd(timex, 2) + "ms";
	}
}

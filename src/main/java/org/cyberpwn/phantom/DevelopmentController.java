package org.cyberpwn.phantom;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.cyberpwn.phantom.clust.Comment;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.DataCluster;
import org.cyberpwn.phantom.clust.Keyed;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.construct.Ticked;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;
import org.cyberpwn.phantom.sync.TaskLater;
import org.cyberpwn.phantom.util.C;
import org.cyberpwn.phantom.util.PluginUtil;

/**
 * 
 * @author cyberpwn
 *
 */
@Ticked(5)
public class DevelopmentController extends Controller implements Configurable
{
	private DataCluster cc;
	private GMap<Plugin, Long> modifications;
	private GMap<Plugin, Long> sizes;
	private GList<String> silence = new GList<String>();
	private GList<String> registered = new GList<String>();

	@Comment("This will reload the desired plugin when it is modified")
	@Keyed("development.reload.on-plugin-change")
	public Boolean reloadOnChanged = false;
	
	@Comment("When true, this will reload all plugins instead of just the modified one")
	@Keyed("development.reload.everything")
	public Boolean reloadEverything = false;
	
	public DevelopmentController(Controllable parentController)
	{
		super(parentController);
		
		this.cc = new DataCluster();
		this.modifications = new GMap<Plugin, Long>();
		this.sizes = new GMap<Plugin, Long>();
		
		new TaskLater(1)
		{
			@Override
			public void run()
			{
				loadCluster(DevelopmentController.this);
			}
		};
	}
	
	public void onTick()
	{
		if(!(reloadOnChanged || reloadEverything))
		{
			return;
		}
		
		for(Plugin i : getPlugin().getServer().getPluginManager().getPlugins())
		{
			try
			{
				File file = new File(getPlugin().getDataFolder().getParentFile(), PluginUtil.getPluginFileName(i.getName()));
			
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
						s("Plugin " + C.LIGHT_PURPLE + " " + i.getName() + " " + C.GREEN + "MODIFIED");
						
						if(reloadEverything)
						{
							s("Reloading All Plugins");
							Bukkit.reload();
						}
						
						else
						{
							s("Reloading " + C.LIGHT_PURPLE + i.getName());
							PluginUtil.reload(i);
						}
					}
				}
			}
			
			catch(Exception e)
			{
				continue;
			}
		}
	}
	
	@Override
	public void onNewConfig()
	{
		cc.set("dispatcher.silence", false, "Should all dispatchers be silenced?");

		for(String i : registered)
		{
			cc.set("dispatcher.nodes." + i.replaceAll(" > ", "."), false);
		}
	}
	
	@Override
	public void onReadConfig()
	{
		for(String i : registered)
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
}

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
import org.cyberpwn.phantom.lang.GMap;
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
		
		loadCluster(this);
	}
	
	public void onTick()
	{
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
		
	}
	
	@Override
	public void onReadConfig()
	{
		
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

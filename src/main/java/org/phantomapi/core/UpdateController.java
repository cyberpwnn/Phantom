package org.phantomapi.core;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.phantomapi.Phantom;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.F;
import org.phantomapi.util.P;
import org.phantomapi.util.PluginUtil;

public class UpdateController extends Controller
{
	private GMap<Plugin, Long> modifications;
	private GMap<Plugin, Long> sizes;
	private GMap<String, String> filePlugins;
	
	public UpdateController(Controllable parentController)
	{
		super(parentController);
		
		modifications = new GMap<Plugin, Long>();
		sizes = new GMap<Plugin, Long>();
		filePlugins = new GMap<String, String>();
	}
	
	@Override
	public void onStart()
	{
		new TaskLater(10)
		{
			@Override
			public void run()
			{
				recalc();
			}
		};
	}
	
	public void recalc()
	{
		for(Plugin i : Bukkit.getServer().getPluginManager().getPlugins())
		{
			String n = getPluginFile(i.getName());
			
			if(n == null)
			{
				continue;
			}
			
			File file = new File(getPlugin().getDataFolder().getParentFile(), n);
			
			if(!modifications.containsKey(i))
			{
				modifications.put(i, file.lastModified());
			}
			
			if(!sizes.containsKey(i))
			{
				sizes.put(i, file.length());
			}
		}
	}
	
	public boolean needsUpdate(Plugin i)
	{
		String n = getPluginFile(i.getName());
		
		if(n == null)
		{
			return false;
		}
		
		File file = new File(getPlugin().getDataFolder().getParentFile(), n);
		
		if(modifications.containsKey(i))
		{
			if(file.lastModified() != modifications.get(i))
			{
				return true;
			}
		}
		
		if(sizes.containsKey(i))
		{
			if(file.length() != sizes.get(i))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void update(PhantomSender p)
	{
		if(p.isPlayer())
		{
			P.showProgress(p.getPlayer(), "Updating;Checking for Modifications");
		}
		
		new TaskLater(1)
		{
			@Override
			public void run()
			{
				GList<Plugin> updates = new GList<Plugin>();
				
				for(Plugin i : Bukkit.getServer().getPluginManager().getPlugins())
				{
					if(needsUpdate(i))
					{
						if(i.getName().equals("Phantom"))
						{
							Phantom.thrash();
							return;
						}
						
						updates.add(i);
					}
				}
				
				double pls = updates.size();
				
				if(p.isPlayer())
				{
					P.showProgress(p.getPlayer(), "Updating " + updates.size() + " Plugins;Checking for Modifications");
				}
				
				new Task(5)
				{
					@Override
					public void run()
					{
						if(updates.isEmpty())
						{
							if(p.isPlayer())
							{
								P.showProgress(p.getPlayer(), "Updating Finished");
								
								new TaskLater(10)
								{
									@Override
									public void run()
									{
										P.clearProgress(p.getPlayer());
									}
								};
							}
							
							new TaskLater()
							{
								@Override
								public void run()
								{
									recalc();
								}
							};
							
							cancel();
							return;
						}
						
						try
						{
							Plugin pl = updates.pop();
							
							if(p.isPlayer())
							{
								P.showProgress(p.getPlayer(), "Updating " + F.pc((pls - updates.size()) / pls) + ";Updating " + pl.getName() + " " + pl.getDescription().getVersion());
							}
							
							String name = pl.getName();
							PluginUtil.unloadNoGC(pl);
							PluginUtil.load(name);
						}
						
						catch(Exception e)
						{
							
						}
					}
				};
			}
		};
	}
	
	private String getPluginFile(String name)
	{
		try
		{
			if(!filePlugins.containsKey(name))
			{
				filePlugins.put(name, PluginUtil.getPluginFileName(name));
			}
			
			return filePlugins.get(name);
		}
		
		catch(Exception e)
		{
			return null;
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
}

package org.cyberpwn.phantom.construct;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.ConfigurationHandler;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.D;

/**
 * A controller
 * @author cyberpwn
 *
 */
public class Controller implements Controllable
{
	protected final GList<Controllable> controllers;
	protected final Controllable parentController;
	protected final ControllablePlugin instance;
	protected final String name;
	protected final D d;
	
	public Controller(Controllable parentController)
	{
		this.controllers = new GList<Controllable>();
		this.parentController = parentController;
		this.name = getClass().getSimpleName();
		this.instance = parentController.getPlugin();
		this.d = new D(toString());
	}
	
	@Override
	public void start()
	{
		for(Controllable i : controllers)
		{
			i.start();
		}
		
		getPlugin().registerListener(this);
		s("Started");
		onStart();
	}
	
	@Override
	public void stop()
	{
		for(Controllable i : controllers)
		{
			i.stop();
		}
		
		getPlugin().unRegisterListener(this);
		s(ChatColor.RED + "Stopped");
		onStop();
	}
	
	@Override
	public void tick()
	{
		onTick();
	}
	
	public void loadCluster(Configurable c)
	{
		loadCluster(c, null);
	}
	
	public void loadCluster(Configurable c, String category)
	{
		File base = getPlugin().getDataFolder();
		
		if(category != null)
		{
			base = new File(base, category);
		}
		
		try
		{
			ConfigurationHandler.read(base, c);
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void register(Controller c)
	{
		controllers.add(c);
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
	public void onTick()
	{
		
	}
	
	@Override
	public GList<Controllable> getControllers()
	{
		return controllers;
	}

	@Override
	public ControllablePlugin getPlugin()
	{
		return instance;
	}
	
	@Override
	public Controllable getParentController()
	{
		return parentController;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		Controllable c = this;
		GList<String> names = new GList<String>();
		
		while(c.getParentController() != null)
		{
			names.add(c.getName());
			c = c.getParentController();
		}
		
		names.add(getPlugin().getName());
		Collections.reverse(names);
		
		return names.toString(" > ");
	}
	
	public void i(String... s)
	{
		d.i(s);
	}
	
	public void s(String... o)
	{
		d.s(o);
	}
	
	public void f(String... o)
	{
		d.f(o);
	}
	
	public void w(String... o)
	{
		d.w(o);
	}
	
	public void v(String... o)
	{
		d.v(o);
	}
	
	public void o(String... o)
	{
		d.o(o);
	}
}

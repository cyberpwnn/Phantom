package org.cyberpwn.phantom.construct;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.ConfigurationHandler;
import org.cyberpwn.phantom.clust.DatabaseConnection;
import org.cyberpwn.phantom.gui.Notification;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.D;

/**
 * A controller
 * 
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
	
	/**
	 * Create a controller as a subcontroller from the parent controller. Tick
	 * rates ARE AFFECTED. For example, if the parent controler tickrate is one
	 * tick per second (20tps), and your tickrate is 0 or 1, your tick rate will
	 * actually be 1 tick per second instead of every tick. This is because the
	 * parent controller ticks your controller. Also, if the parent controller
	 * is not ticked, then this controller can not tick even with the ticked
	 * annotation
	 * 
	 * @param parentController
	 *            the parent controller
	 */
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
	
	/**
	 * Load a data cluster from file This will also create the file and add in
	 * default values if it doesnt exist
	 * 
	 * @param c
	 *            the configurable object
	 */
	public void loadCluster(Configurable c)
	{
		loadCluster(c, null);
	}
	
	/**
	 * Load a data cluster from file This will also create the file and add in
	 * default values if it doesnt exist
	 * 
	 * @param c
	 *            the configurable object
	 * @param category
	 *            the category
	 */
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
	
	/**
	 * Load data from a mysql database. If it doesnt exists, nothing will be
	 * added to the cluster, and nothing will be created in the database
	 * Requires the Tabled annotation
	 * 
	 * @param c
	 *            the configurable object
	 * @param connection
	 *            the database connection data
	 */
	public void loadMysql(Configurable c, DatabaseConnection connection)
	{
		if(!ConfigurationHandler.hasTable(c))
		{
			return;
		}
		
		try
		{
			ConfigurationHandler.readMySQL(c, connection);
		}
		
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves data to a mysql database. Requires the Tabled annotation
	 * 
	 * @param c
	 *            the configurable object
	 * @param connection
	 *            the database connection data
	 */
	public void saveMysql(Configurable c, DatabaseConnection connection)
	{
		if(!ConfigurationHandler.hasTable(c))
		{
			return;
		}
		
		try
		{
			ConfigurationHandler.saveMySQL(c, connection);
		}
		
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * save a data cluster to the file This will also create the file and add in
	 * default values if it doesnt exist. New values will be added in aswell for
	 * updating configs
	 * 
	 * @param c
	 *            the configurable object
	 */
	public void saveCluster(Configurable c)
	{
		saveCluster(c, null);
	}
	
	/**
	 * save a data cluster to the file This will also create the file and add in
	 * default values if it doesnt exist. New values will be added in aswell for
	 * updating configs
	 * 
	 * @param c
	 *            the configurable object
	 * @param category
	 *            the category
	 */
	public void saveCluster(Configurable c, String category)
	{
		File base = getPlugin().getDataFolder();
		
		if(category != null)
		{
			base = new File(base, category);
		}
		
		try
		{
			ConfigurationHandler.save(base, c);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Queue a notification to a given player
	 * 
	 * @param p
	 *            the player
	 * @param n
	 *            the notification
	 */
	public void queueNotification(Player p, Notification n)
	{
		Phantom.queueNotification(p, n);
	}
	
	/**
	 * Queue a notification to all players
	 * 
	 * @param n
	 *            the notification
	 */
	public void queueNotification(Notification n)
	{
		Phantom.queueNotification(n);
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

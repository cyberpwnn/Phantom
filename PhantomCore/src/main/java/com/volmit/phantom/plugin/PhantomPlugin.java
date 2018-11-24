package com.volmit.phantom.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.volmit.phantom.time.M;

public class PhantomPlugin extends JavaPlugin
{
	public static PhantomPlugin plugin;
	
	public PhantomPlugin()
	{
		super();
		plugin = this;
		M.initTicking();
	}
	
	@Override
	public void onEnable()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> tick(), 0, 0);
	}

	@Override
	public void onDisable()
	{

	}
	
	public void tick()
	{
		
	}
}

package com.volmit.phantom.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.volmit.phantom.lang.D;
import com.volmit.phantom.time.M;

public class PhantomPlugin extends JavaPlugin
{
	private PawnManager manager;
	public static PhantomPlugin plugin;
	
	public PhantomPlugin()
	{
		super();
		plugin = this;
		manager = new PawnManager();
		D.ll("Starting Phantom");
		M.initTicking();
	}
	
	public PawnManager getPawnManager()
	{
		return manager;
	}
	
	@Override
	public void onEnable()
	{
		D.ll("Phantom Online");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> tick(), 0, 0);
	}

	@Override
	public void onDisable()
	{

	}
	
	public void tick()
	{
		M.uptick();

		if(M.interval(20))
		{
			Phantom.flushLogBuffer();
		}
	}
}

package com.volmit.phantom.main;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.volmit.phantom.api.job.J;
import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.math.M;

public class PhantomPlugin extends JavaPlugin implements Listener
{
	public static PhantomPlugin plugin;

	public PhantomPlugin()
	{
		super();
		plugin = this;
	}

	@Override
	public void onEnable()
	{
		D.ll("Starting Phantom");
		M.initTicking();
		J.executeAfterStartupQueue();
		Phantom.suckerpunch();
		D.ll("Phantom Online");
	}

	@Override
	public void onDisable()
	{
		Phantom.getModuleManager().unloadModules();
		Phantom.flushLogBuffer();
		Phantom.stopAllServices();
		Phantom.flushLogBuffer();
	}
}

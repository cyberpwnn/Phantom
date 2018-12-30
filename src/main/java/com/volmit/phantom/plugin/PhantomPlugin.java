package com.volmit.phantom.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.volmit.phantom.lang.D;
import com.volmit.phantom.time.M;

public class PhantomPlugin extends JavaPlugin implements Listener
{
	public static PhantomPlugin plugin;

	public PhantomPlugin()
	{
		super();
		plugin = this;
		D.ll("Starting Phantom");
		M.initTicking();
		Phantom.taskManager = new TaskManager(this);
		Phantom.moduleManager = new ModuleManager();
		Phantom.moduleManager.start();
	}

	@Override
	public void onEnable()
	{
		D.ll("Phantom Online");
		Phantom.flushLogBuffer();
		Phantom.taskManager.syncRepeating(() -> tick());
		Phantom.dumpStartup();
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerCommandPreprocessEvent e)
	{
		if(e.getMessage().equalsIgnoreCase("/tests") && e.getPlayer().isOp())
		{
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(ServerCommandEvent e)
	{
		if(e.getCommand().equalsIgnoreCase("tests"))
		{
			e.setCancelled(true);
		}
	}

	@Override
	public void onDisable()
	{
		Phantom.moduleManager.stopModules();
		Phantom.flushLogBuffer();
		Phantom.stopAllServices();
		Phantom.moduleManager.unloadModules();
		Phantom.flushLogBuffer();
	}

	public void tick()
	{
		M.uptick();

		if(M.interval(15))
		{
			Phantom.flushLogBuffer();
		}
	}
}

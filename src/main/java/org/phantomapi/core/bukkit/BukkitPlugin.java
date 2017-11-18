package org.phantomapi.core.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitPlugin extends JavaPlugin
{
	public BukkitPlugin()
	{
		onInit();
	}

	public abstract void onInit();

	@Override
	public abstract void onLoad();

	@Override
	public abstract void onEnable();

	@Override
	public abstract void onDisable();
}

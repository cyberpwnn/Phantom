package org.phantomapi.construct;

import java.io.InputStream;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class WrappedJavaPlugin extends JavaPlugin
{
	protected JavaPlugin plugin;
	
	public WrappedJavaPlugin(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public PluginCommand getCommand(String name)
	{
		return plugin.getCommand(name);
	}
	
	@Override
	public FileConfiguration getConfig()
	{
		return plugin.getConfig();
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		return plugin.getDefaultWorldGenerator(worldName, id);
	}
	
	@Override
	public InputStream getResource(String filename)
	{
		return plugin.getResource(filename);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return plugin.onCommand(sender, command, label, args);
	}
	
	@Override
	public void onDisable()
	{
		plugin.onDisable();
	}
	
	@Override
	public void onEnable()
	{
		plugin.onEnable();
	}
	
	@Override
	public void onLoad()
	{
		plugin.onLoad();
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return plugin.onTabComplete(sender, command, alias, args);
	}
	
	@Override
	public void reloadConfig()
	{
		plugin.reloadConfig();
	}
	
	@Override
	public void saveConfig()
	{
		plugin.saveConfig();
	}
	
	@Override
	public void saveDefaultConfig()
	{
		plugin.saveDefaultConfig();
	}
	
	@Override
	public void saveResource(String resourcePath, boolean replace)
	{
		plugin.saveResource(resourcePath, replace);
	}
	
	@Override
	public String toString()
	{
		return plugin.toString();
	}
}

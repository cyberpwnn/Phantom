package org.cyberpwn.phantom.network;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * Like this
 * 
 * new PluginMessage(this, "GetServer").send();
 * 
 * @author cyberpwn
 *
 */
public class PluginMessage
{
	protected ByteArrayDataOutput out;
	private Player player;
	private Plugin plugin;
	
	/**
	 * Sends a plugin message
	 * 
	 * @param sender
	 *            the plugin who sends it
	 * @param values
	 *            the data to send (string channel, string modifiers...)
	 */
	public PluginMessage(Plugin sender, String... values)
	{
		out = ByteStreams.newDataOutput();
		
		for(String i : values)
		{
			out.writeUTF(i);
		}
		
		plugin = sender;
		player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
	}
	
	/**
	 * Sends the plugin message
	 */
	public void send()
	{
		if(player == null)
		{
			return;
		}
		
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
}

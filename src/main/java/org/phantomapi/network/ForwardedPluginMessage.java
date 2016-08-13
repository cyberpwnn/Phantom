package org.phantomapi.network;

import java.io.ByteArrayOutputStream;

import org.bukkit.plugin.Plugin;

/**
 * Forwarded messages
 * 
 * @author cyberpwn
 *
 */
public class ForwardedPluginMessage extends PluginMessage
{
	/**
	 * Send a forwarded plugin message
	 * 
	 * @param sender
	 *            the plugin sender
	 * @param channel
	 *            the channel
	 * @param destination
	 *            what server to send it to
	 * @param data
	 *            the data
	 */
	public ForwardedPluginMessage(Plugin sender, String channel, String destination, ByteArrayOutputStream data)
	{
		super(sender, "Forward", destination, channel);
		
		out.writeShort(data.toByteArray().length);
		out.write(data.toByteArray());
	}
	
	/**
	 * Send a forwarded plugin message to all servers including this one
	 * 
	 * @param sender
	 *            the plugin sender
	 * @param channel
	 *            the channel
	 * @param data
	 *            the data
	 */
	public ForwardedPluginMessage(Plugin sender, String channel, ByteArrayOutputStream data)
	{
		this(sender, channel, "ALL", data);
	}
}

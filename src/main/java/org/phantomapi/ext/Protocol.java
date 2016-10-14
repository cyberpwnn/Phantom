package org.phantomapi.ext;

import org.bukkit.entity.Player;

/**
 * Protocol mapping and versioning
 * 
 * @author cyberpwn
 */
public enum Protocol
{
	/**
	 * The version is either unknown, unsupported, or viaversion is not
	 * installed on the server
	 */
	UNSUPPORTED("UNSUPPORTED", -1),
	
	/**
	 * 1.7.6 - 1.7.10
	 */
	V7("1.7", 5),
	
	/**
	 * 1.8 - 1.8.9
	 */
	V8("1.8", 47),
	
	/**
	 * 1.9
	 */
	V9("1.9", 107),
	
	/**
	 * 1.9.1
	 */
	V91("1.9.1", 108),
	
	/**
	 * 1.9.2
	 */
	V92("1.9.2", 109),
	
	/**
	 * 1.9.3 - 1.9.4
	 */
	V93_4("1.9.3/4", 110),
	
	/**
	 * 1.10 - 1.10.2
	 */
	V110("1.10", 210);
	
	private String name;
	private int version;
	
	private Protocol(String name, int version)
	{
		this.name = name;
		this.version = version;
	}
	
	/**
	 * Gets the formatted name of the protocol version
	 */
	public String toString()
	{
		return name;
	}
	
	/**
	 * Get the protocol name
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get the protocol version
	 * 
	 * @return the version
	 */
	public int getVersion()
	{
		return version;
	}
	
	/**
	 * Get the player's protocol
	 * 
	 * @param player
	 *            the player
	 * @return the protocol
	 */
	public static Protocol getProtocol(Player player)
	{
		int m = new ViaVersionPluginConnector().getProtocol(player);
		
		if(m == -1)
		{
			return Protocol.UNSUPPORTED;
		}
		
		for(Protocol i : Protocol.values())
		{
			if(m == i.getVersion())
			{
				return i;
			}
		}
		
		return Protocol.UNSUPPORTED;
	}
}

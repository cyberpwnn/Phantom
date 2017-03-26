package org.phantomapi.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PacketSoundEvent extends OutboundPacketEvent
{
	private Location location;
	private float volume;
	private float pitch;
	
	public PacketSoundEvent(Player player, Location location, float volume, float pitch)
	{
		super(player);
		
		this.location = location;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
	
	public float getVolume()
	{
		return volume;
	}
	
	public void setVolume(float volume)
	{
		this.volume = volume;
	}
	
	public float getPitch()
	{
		return pitch;
	}
	
	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}
}

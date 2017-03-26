package org.phantomapi.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PacketCustomSoundEvent extends PacketSoundEvent
{
	private String sound;
	
	public PacketCustomSoundEvent(Player player, String sound, Location location, float volume, float pitch)
	{
		super(player, location, volume, pitch);
		
		this.sound = sound;
	}
	
	public String getSound()
	{
		return sound;
	}
	
	public void setSound(String sound)
	{
		this.sound = sound;
	}
}

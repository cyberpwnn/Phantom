package org.phantomapi.event;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PacketNamedSoundEvent extends PacketSoundEvent
{
	private Sound sound;
	
	public PacketNamedSoundEvent(Player player, Sound sound, Location location, float volume, float pitch)
	{
		super(player, location, volume, pitch);
		
		this.sound = sound;
	}
	
	public Sound getSound()
	{
		return sound;
	}
	
	public void setSound(Sound sound)
	{
		this.sound = sound;
	}
}

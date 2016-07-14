package org.cyberpwn.phantom.sfx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface Audible
{
	public void play(Player p, Location l);
	
	public void play(Player p);
	
	public void play(Location l);
	
	public void play(Player p, Vector v);

	public Float getVolume();
	
	public void setVolume(Float volume);
	
	public Float getPitch();
	
	public void setPitch(Float pitch);
	
	public Audible clone();
}

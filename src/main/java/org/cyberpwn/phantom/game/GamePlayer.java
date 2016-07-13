package org.cyberpwn.phantom.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface GamePlayer<M, G, T, P>
{
	public G getGame();
	
	public T getTeam();
	
	public boolean isInMap();
	
	public Location getLocation();
	
	public void setTeam(T t);
	
	public Player getPlayer();
}

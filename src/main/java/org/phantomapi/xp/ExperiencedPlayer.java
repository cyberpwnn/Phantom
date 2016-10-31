package org.phantomapi.xp;

import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.clust.Tabled;

@Tabled("experienced_player")
public class ExperiencedPlayer extends ConfigurableObject
{
	private Player player;
	
	@Keyed("x")
	public long experience = 0;
	
	@Keyed("b")
	public double boost = 0;
	
	public ExperiencedPlayer(Player p)
	{
		super(p.getUniqueId().toString());
		
		player = p;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public long getExperience()
	{
		return experience;
	}
	
	public double getBoost()
	{
		return boost;
	}
	
	public void setExperience(long experience)
	{
		this.experience = experience;
	}
	
	public void setBoost(double boost)
	{
		this.boost = boost;
	}
}

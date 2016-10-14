package org.phantomapi.currency;

import org.bukkit.entity.Player;
import org.phantomapi.util.P;

/**
 * Experience currency implementation
 * 
 * @author cyberpwn
 */
public class ExperienceCurrency implements Currency
{
	@Override
	public double get(Player p)
	{
		return P.getTotalExperience(p);
	}
	
	@Override
	public void give(Player p, double amt)
	{
		P.setTotalExperience(p, (int) (amt + get(p)));
	}
	
	@Override
	public void take(Player p, double amt)
	{
		P.setTotalExperience(p, (int) (get(p) - amt));
	}
	
	@Override
	public String getSuffix()
	{
		return " XP";
	}
	
	@Override
	public String getPrefix()
	{
		return "";
	}
}

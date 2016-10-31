package org.phantomapi.xp;

import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.currency.Currency;

/**
 * Experience currency implementation
 * 
 * @author cyberpwn
 */
public class XPCurrency implements Currency
{
	@Override
	public double get(Player p)
	{
		return Phantom.instance().getXpController().getEdc().get(p).getExperience();
	}
	
	@Override
	public void give(Player p, double amt)
	{
		Phantom.instance().getXpController().getEdc().get(p).setExperience((long) (amt + get(p)));
	}
	
	@Override
	public void take(Player p, double amt)
	{
		Phantom.instance().getXpController().getEdc().get(p).setExperience((long) (get(p) - amt));
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

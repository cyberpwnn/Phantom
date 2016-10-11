package org.phantomapi.currency;

import org.bukkit.entity.Player;
import org.phantomapi.Phantom;

/**
 * Vault currency implementation
 * 
 * @author cyberpwn
 */
public class VaultCurrency implements Currency
{
	@Override
	public double get(Player p)
	{
		return Phantom.instance().getEcon().getBalance(p);
	}
	
	@Override
	public void give(Player p, double amt)
	{
		Phantom.instance().getEcon().depositPlayer(p, amt);
	}
	
	@Override
	public void take(Player p, double amt)
	{
		Phantom.instance().getEcon().withdrawPlayer(p, amt);
	}
	
	@Override
	public String getSuffix()
	{
		return "";
	}
	
	@Override
	public String getPrefix()
	{
		return "$";
	}
}

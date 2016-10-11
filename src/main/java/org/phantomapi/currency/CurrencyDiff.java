package org.phantomapi.currency;

import org.bukkit.entity.Player;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

/**
 * Currency diff
 * 
 * @author cyberpwn
 */
public class CurrencyDiff implements CurrencyMessager
{
	private Currency currency;
	
	public CurrencyDiff(Currency currency)
	{
		this.currency = currency;
	}
	
	@Override
	public String earned(Player p, double amt, double total)
	{
		return C.GREEN + "+ " + currency.getPrefix() + F.f((int) amt) + currency.getSuffix() + " " + current(p, total);
	}
	
	@Override
	public String spent(Player p, double amt, double total)
	{
		return C.RED + "- " + currency.getPrefix() + F.f((int) amt) + currency.getSuffix() + " " + current(p, total);
	}
	
	@Override
	public String current(Player p, double total)
	{
		return C.DARK_GRAY + "(" + currency.getPrefix() + F.f((int) total) + currency.getSuffix() + ")";
	}
	
	@Override
	public Currency getCurrency()
	{
		return currency;
	}
	
}

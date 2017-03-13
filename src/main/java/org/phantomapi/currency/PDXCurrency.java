package org.phantomapi.currency;

import org.bukkit.entity.Player;
import org.phantomapi.clust.PD;

/**
 * PDX currency implementation
 * 
 * @author cyberpwn
 */
public class PDXCurrency implements Currency
{
	private String id;
	
	public PDXCurrency(String id)
	{
		this.id = id;
	}
	
	@Override
	public double get(Player p)
	{
		return PD.get(p).getConfiguration().contains("cu." + id) ? PD.get(p).getConfiguration().getDouble("cu." + id) : 0;
	}
	
	@Override
	public void give(Player p, double amt)
	{
		PD.get(p).getConfiguration().set("cu." + id, get(p) + amt);
	}
	
	@Override
	public void take(Player p, double amt)
	{
		PD.get(p).getConfiguration().set("cu." + id, get(p) - amt);
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
	
	@Override
	public String getId()
	{
		return id;
	}
}

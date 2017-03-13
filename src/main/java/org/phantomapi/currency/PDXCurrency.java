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
	private String prefix;
	private String suffix;
	
	public PDXCurrency(String id, String prefix, String suffix)
	{
		this.id = id;
		this.prefix = prefix;
		this.suffix = suffix;
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
		return suffix;
	}
	
	@Override
	public String getPrefix()
	{
		return prefix;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
}

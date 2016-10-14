package org.phantomapi.inventory;

import java.util.Collection;
import org.bukkit.potion.PotionEffect;

public class PotionData
{
	private boolean splash;
	private int level;
	private Collection<PotionEffect> effects;
	
	public PotionData(boolean splash, int level, Collection<PotionEffect> effects)
	{
		this.splash = splash;
		this.level = level;
		this.effects = effects;
	}
	
	public boolean isSplash()
	{
		return splash;
	}
	
	public void setSplash(boolean splash)
	{
		this.splash = splash;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public Collection<PotionEffect> getEffects()
	{
		return effects;
	}
	
	public void setEffects(Collection<PotionEffect> effects)
	{
		this.effects = effects;
	}
}

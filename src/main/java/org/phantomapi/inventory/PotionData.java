package org.phantomapi.inventory;

import java.io.IOException;
import java.util.Collection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataEntity;
import org.phantomapi.lang.GList;

public class PotionData implements DataEntity
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
	
	@SuppressWarnings("deprecation")
	@Override
	public byte[] toData() throws IOException
	{
		DataCluster cc = new DataCluster();
		GList<String> e = new GList<String>();
		
		for(PotionEffect i : effects)
		{
			e.add(i.getType().getId() + ";" + i.getAmplifier() + ";" + i.getDuration());
		}
		
		cc.set("s", splash);
		cc.set("l", level);
		cc.set("e", e);
		
		return cc.compress();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		DataCluster cc = new DataCluster(data);
		splash = cc.getBoolean("s");
		level = cc.getInt("l");
		effects.clear();
		
		for(String i : cc.getStringList("e"))
		{
			@SuppressWarnings("deprecation")
			PotionEffect pe = new PotionEffect(PotionEffectType.getById(Integer.valueOf(i.split(";")[0])), Integer.valueOf(i.split(";")[2]), Integer.valueOf(i.split(";")[1]));
			effects.add(pe);
		}
	}
}

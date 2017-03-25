package org.phantomapi.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;

/**
 * Create a multiline hologram built from ArmorStandHologram instances
 * 
 * @author cyberpwn
 */
public class PhantomHologram implements Hologram
{
	private GList<Hologram> meta;
	private String text;
	private Location base;
	private double splitDistance;
	private Player exclusive;
	
	/**
	 * Create a phantom hologram with the given base location as the bottom
	 * armor stand
	 * 
	 * @param base
	 *            the base location
	 */
	public PhantomHologram(Location base)
	{
		exclusive = null;
		meta = new GList<Hologram>();
		text = null;
		this.base = base;
		splitDistance = 0.25;
	}
	
	@Override
	public Entity getHandle()
	{
		return meta.get(0).getHandle();
	}
	
	@Override
	public String getDisplay()
	{
		return text;
	}
	
	@Override
	public void setLocation(Location location)
	{
		base = location;
		update();
	}
	
	@Override
	public void setTextLocation(Location location)
	{
		setLocation(location.clone().add(0, -1.6, 0));
	}
	
	@Override
	public void setDisplay(String display)
	{
		text = display;
		update();
	}
	
	/**
	 * Update this holgram's instances, texts and locations. This is
	 * automatically called
	 */
	public void update()
	{
		int a = 1;
		
		if(text.contains("\n"))
		{
			a = text.split("\n").length;
		}
		
		while(meta.size() > a)
		{
			meta.get(meta.size() - 1).getHandle().remove();
			meta.remove(meta.size() - 1);
		}
		
		while(meta.size() < a)
		{
			meta.add(new ArmorStandHologram(base.clone().add(0, meta.size() * splitDistance, 0)));
		}
		
		int k = 0;
		
		for(Hologram i : meta)
		{
			i.setLocation(base.clone().add(0, (k++ * splitDistance), 0));
		}
		
		int ind = meta.size();
		
		if(text.contains("\n"))
		{
			for(String i : text.split("\n"))
			{
				meta.get(--ind).setDisplay(i);
			}
		}
		
		else
		{
			meta.get(0).setDisplay(text);
		}
	}
	
	@Override
	public void destroy()
	{
		for(Hologram i : meta)
		{
			i.destroy();
		}
		
		meta.clear();
		text = null;
	}
	
	@Override
	public Location getLocation()
	{
		return base;
	}
	
	public double getSplitDistance()
	{
		return splitDistance;
	}
	
	public void setSplitDistance(double splitDistance)
	{
		this.splitDistance = splitDistance;
		update();
	}
	
	@Override
	public void setExclusive(Player p)
	{
		exclusive = p;
		
		for(Hologram i : meta)
		{
			i.setExclusive(p);
		}
	}
	
	@Override
	public Player getExclusive()
	{
		return exclusive;
	}
}

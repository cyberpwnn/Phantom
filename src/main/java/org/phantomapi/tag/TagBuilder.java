package org.phantomapi.tag;

import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftAreaEffectCloud;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.nms.NMSX;
import net.minecraft.server.v1_9_R2.EntityAreaEffectCloud;

public class TagBuilder
{
	private final GList<String> context;
	private final GMap<String, AreaEffectCloud> locks;
	private final Entity base;
	private TaggedPlayer t;
	
	public TagBuilder(Entity base, TaggedPlayer taggedPlayer)
	{
		locks = new GMap<String, AreaEffectCloud>();
		this.base = base;
		context = new GList<String>();
		t = taggedPlayer;
	}
	
	public TagBuilder add(String context)
	{
		this.context.add(context);
		return this;
	}
	
	public void destroyContext()
	{
		context.clear();
		
		for(String i : locks.k())
		{
			locks.get(i).remove();
		}
		
		locks.clear();
	}
	
	public void rebuild()
	{
		if(!t.isTagged())
		{
			return;
		}
		
		for(String i : locks.k())
		{
			locks.get(i).remove();
		}
		
		locks.clear();
		build();
	}
	
	public void build()
	{
		if(!t.isTagged())
		{
			return;
		}
		
		Entity last = base;
		
		int k = 3;
		double amx = 0.375;
		double amv = -0.161;
		
		for(String i : context)
		{
			k++;
			locks.put(i, (AreaEffectCloud) base.getWorld().spawnEntity(base.getLocation().clone().add(0, (k * amx) + amv, 0), EntityType.AREA_EFFECT_CLOUD));
			locks.get(i).setRadius(0f);
			locks.get(i).setRadiusOnUse(0f);
			locks.get(i).setRadiusPerTick(0f);
			locks.get(i).setParticle(Particle.SUSPENDED);
			locks.get(i).setInvulnerable(true);
			locks.get(i).setDuration(Integer.MAX_VALUE / 20);
			locks.get(i).setCustomName(i);
			locks.get(i).setCustomNameVisible(true);
			NMSX.addPassenger(last, locks.get(i));
			last = locks.get(i);
			
			if(base instanceof Player)
			{
				NMSX.hideEntity((Player) base, locks.get(i));
			}
		}
	}
	
	public void fortify()
	{
		if(!t.isTagged())
		{
			return;
		}
		
		for(String i : context)
		{
			locks.get(i).setDuration(Integer.MAX_VALUE / 20);
			
			if(locks.get(i).isDead())
			{
				rebuild();
				return;
			}
		}
	}
	
	public void setSneak(boolean b)
	{
		if(!t.isTagged())
		{
			return;
		}
		
		for(String i : locks.k())
		{
			EntityAreaEffectCloud ae = ((CraftAreaEffectCloud) locks.get(i)).getHandle();
			ae.setSneaking(b);
		}
	}
	
	public void update()
	{
		if(!t.isTagged())
		{
			return;
		}
		
		if(!locks.k().equals(context))
		{
			if(locks.size() != context.size())
			{
				rebuild();
			}
			
			else
			{
				for(String i : context)
				{
					if(!locks.containsKey(i))
					{
						for(String j : locks.k())
						{
							if(!context.contains(j))
							{
								AreaEffectCloud a = locks.get(j);
								locks.remove(j);
								locks.put(i, a);
								locks.get(i).setCustomName(i);
							}
						}
					}
				}
			}
		}
		
		fortify();
	}
	
	public GList<String> getContext()
	{
		return context;
	}
	
	public GMap<String, AreaEffectCloud> getLocks()
	{
		return locks;
	}
	
	public Entity getBase()
	{
		return base;
	}
}

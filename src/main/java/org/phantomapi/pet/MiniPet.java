package org.phantomapi.pet;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.Phantom;
import org.phantomapi.sync.Task;
import org.phantomapi.util.P;
import org.phantomapi.world.PE;

public abstract class MiniPet implements Listener
{
	private Zombie z;
	private boolean following;
	private boolean follow;
	
	public MiniPet(Location location, ItemStack skull, String name)
	{
		follow = true;
		following = false;
		z = (Zombie) EntityTypes.spawnEntity(new MiniEntityPet(((CraftWorld) location.getWorld()).getHandle()), location);
		z.setInvulnerable(true);
		PE.INVISIBILITY.a(0).d(10000000).c(z);
		z.getEquipment().setHelmet(skull);
		Phantom.instance().registerListener(this);
		
		new Task(5)
		{
			@Override
			public void run()
			{
				if(z.isDead())
				{
					Phantom.instance().unRegisterListener(MiniPet.this);
					cancel();
					
					return;
				}
				
				if(follow != following)
				{
					if(follow)
					{
						new NMSPetUtils().setToFollow(z, P.getAnyPlayer().getUniqueId(), 1f);
					}
					
					else
					{
						new NMSPetUtils().setToFollow(z, P.getAnyPlayer().getUniqueId(), 0f);
					}
					
					following = follow;
				}
				
				PE.INVISIBILITY.a(0).d(10000000).c(z);
				entityTick(z);
			}
		};
	}
	
	@EventHandler
	public void on(EntityDamageEvent e)
	{
		if(e.getEntity().getEntityId() == z.getEntityId())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerInteractAtEntityEvent e)
	{
		if(e.getRightClicked().getEntityId() == z.getEntityId())
		{
			entityInteract(z);
		}
	}
	
	public abstract void entityInteract(Zombie z);
	
	public abstract void entityTick(Zombie z);
	
	public boolean isFollow()
	{
		return follow;
	}
	
	public void setFollow(boolean follow)
	{
		this.follow = follow;
	}
}

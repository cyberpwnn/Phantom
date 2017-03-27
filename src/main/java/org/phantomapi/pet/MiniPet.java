package org.phantomapi.pet;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.Phantom;
import org.phantomapi.event.PacketNamedSoundEvent;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSound;
import org.phantomapi.sync.Task;
import org.phantomapi.world.PE;
import org.phantomapi.world.W;

public abstract class MiniPet implements Listener
{
	private Zombie z;
	private Snowball s;
	private Snowball s2;
	private boolean following;
	private boolean follow;
	private GSound ambientSound;
	private double minDist;
	private Player owner;
	private String name;
	
	public MiniPet(Player owner, Location location, ItemStack skull, String name)
	{
		follow = true;
		following = false;
		z = (Zombie) EntityTypes.spawnEntity(new MiniEntityPet(((CraftWorld) location.getWorld()).getHandle()), location);
		z.setInvulnerable(true);
		z.getEquipment().setHelmet(skull);
		Phantom.instance().registerListener(this);
		ambientSound = new GSound(Sound.ENTITY_WOLF_AMBIENT, 1f, 1.7f);
		minDist = 3.7;
		s = (Snowball) z.getWorld().spawnEntity(z.getLocation().clone().add(0, 1, 0), EntityType.SNOWBALL);
		s2 = (Snowball) z.getWorld().spawnEntity(z.getLocation().clone().add(0, 1, 0), EntityType.SNOWBALL);
		s2.setCustomName(name);
		s2.setCustomNameVisible(true);
		s.setPassenger(s2);
		z.setPassenger(s);
		this.name = name;
		PE.INVISIBILITY.a(0).d(10000000).c(z);
		this.owner = owner;
		
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
						new NMSPetUtils().setToFollow(z, owner.getUniqueId(), 1f);
						
						if(!owner.getWorld().equals(z.getWorld()))
						{
							z.teleport(owner.getLocation());
							entityTeleported(z);
						}
					}
					
					else
					{
						new NMSPetUtils().setToFollow(z, owner.getUniqueId(), 0f);
					}
					
					following = follow;
				}
				
				PE.INVISIBILITY.a(0).d(10000000).c(z);
				s2.setCustomName(getName());
				
				if(!owner.getWorld().equals(z.getWorld()))
				{
					z.teleport(owner);
				}
				
				if(owner.getLocation().distanceSquared(z.getEyeLocation()) < minDist * minDist)
				{
					follow = false;
				}
				
				else
				{
					follow = true;
				}
				
				entityTick(z);
			}
		};
	}
	
	public void destroy()
	{
		z.remove();
		s.remove();
		s2.remove();
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
		if(e.getRightClicked().getEntityId() == z.getEntityId() && e.getPlayer().equals(owner))
		{
			entityInteract(z);
		}
	}
	
	@EventHandler
	public void on(PacketNamedSoundEvent e)
	{
		if(e.getSound().equals(Sound.ENTITY_ZOMBIE_AMBIENT))
		{
			GList<Location> scan = new GList<Location>();
			Block b = e.getLocation().getBlock();
			scan.add(b.getLocation());
			
			for(Block i : W.blockFaces(b))
			{
				scan.add(i.getLocation());
			}
			
			if(scan.contains(z.getLocation().getBlock().getLocation()))
			{
				entityAmbient(z);
				
				if(ambientSound != null)
				{
					e.setSound(ambientSound.getiSound());
					e.setVolume(ambientSound.getVolume());
					e.setPitch(ambientSound.getPitch());
				}
				
				else
				{
					e.setCancelled(true);
				}
			}
		}
		
		if(e.getSound().equals(Sound.ENTITY_ZOMBIE_STEP))
		{
			e.setCancelled(true);
		}
	}
	
	public abstract void entityInteract(Zombie z);
	
	public abstract void entityTick(Zombie z);
	
	public abstract void entityTeleported(Zombie z);
	
	public abstract void entityAmbient(Zombie z);
	
	public boolean isFollow()
	{
		return follow;
	}
	
	public void setFollow(boolean follow)
	{
		this.follow = follow;
	}
	
	public GSound getAmbientSound()
	{
		return ambientSound;
	}
	
	public void setAmbientSound(GSound ambientSound)
	{
		this.ambientSound = ambientSound;
	}
	
	public double getMinDist()
	{
		return minDist;
	}
	
	public void setMinDist(double minDist)
	{
		this.minDist = minDist;
	}
	
	public Zombie getZ()
	{
		return z;
	}
	
	public boolean isFollowing()
	{
		return following;
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	public Snowball getS()
	{
		return s;
	}
	
	public Snowball getS2()
	{
		return s2;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
}

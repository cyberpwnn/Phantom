package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import de.inventivegames.npc.NPC;
import de.inventivegames.npc.NPCLib;
import de.inventivegames.npc.equipment.EquipmentSlot;

public class PhantomNPCWrapper implements NPCWrapper
{
	private NPC npc;
	private String name;
	private String skin;
	
	public PhantomNPCWrapper(String name, String skin)
	{
		this.npc = null;
		this.name = name;
		this.skin = skin;
	}
	
	public PhantomNPCWrapper(String name)
	{
		this(name, name);
	}
	
	public void spawn(Location location)
	{
		this.npc = NPCLib.spawnPlayerNPC(location, name, skin);
	}
	
	public void despawn()
	{
		npc.despawn();
		npc = null;
	}
	
	public boolean isSpawned()
	{
		return npc != null;
	}
	
	public Location getLocation()
	{
		if(isSpawned())
		{
			Location location = npc.getLocation();
			location.setYaw(getYaw());
			location.setPitch(getPitch());
			
			return location;
		}
		
		return null;
	}
	
	public void teleport(Location location)
	{
		if(isSpawned())
		{
			npc.teleport(location);
			setYawPitch(location.getYaw(), location.getPitch());
		}
	}
	
	public Entity getTarget()
	{
		if(isSpawned())
		{
			return npc.getTarget();
		}
		
		return null;
	}
	
	public void setTarget(Entity entity)
	{
		if(isSpawned())
		{
			npc.setTarget(entity);
		}
	}
	
	public LivingEntity getEntity()
	{
		if(isSpawned())
		{
			return npc.getBukkitEntity();
		}
		
		return null;
	}
	
	public int getEntityId()
	{
		if(isSpawned())
		{
			return npc.getEntityID();
		}
		
		return -1;
	}
	
	public void navigateTo(Location location)
	{
		if(isSpawned())
		{
			npc.pathfindTo(location);
		}
	}
	
	public void look(Location location)
	{
		if(isSpawned())
		{
			npc.lookAt(location);
		}
	}
	
	public void setPassenger(Entity entity)
	{
		if(isSpawned())
		{
			npc.setPassenger(entity);
		}
	}
	
	public Entity getPassenger()
	{
		if(isSpawned())
		{
			return npc.getPassenger();
		}
		
		return null;
	}
	
	public void setYawPitch(float yaw, float pitch)
	{
		setYaw(yaw);
		setPitch(pitch);
	}
	
	public void setYaw(float yaw)
	{
		if(isSpawned())
		{
			npc.setYaw(yaw);
		}
	}
	
	public Float getYaw()
	{
		if(isSpawned())
		{
			return npc.getYaw();
		}
		
		return null;
	}
	
	public void setPitch(float pitch)
	{
		if(isSpawned())
		{
			npc.setPitch(pitch);
		}
	}
	
	public Float getPitch()
	{
		if(isSpawned())
		{
			return npc.getPitch();
		}
		
		return null;
	}
	
	public void setEquipment(WraithEquipment slot, ItemStack item)
	{
		if(isSpawned())
		{
			npc.setEquipment(EquipmentSlot.valueOf(slot.toString()), item);
		}
	}
	
	public ItemStack getEquipment(WraithEquipment slot)
	{
		if(isSpawned())
		{
			return npc.getEquipment(EquipmentSlot.valueOf(slot.toString()));
		}
		
		return null;
	}
	
	public void setInvulnerable(boolean invulnerable)
	{
		if(isSpawned())
		{
			npc.setInvulnerable(invulnerable);
		}
	}
	
	public Boolean isInvulnerable()
	{
		if(isSpawned())
		{
			return npc.isInvulnerable();
		}
		
		return null;
	}
	
	public void setCollision(boolean collision)
	{
		if(isSpawned())
		{
			npc.setCollision(collision);
		}
	}
	
	public Boolean hasCollision()
	{
		if(isSpawned())
		{
			return npc.hasCollision();
		}
		
		return null;
	}
	
	public void setControllable(boolean controllable)
	{
		if(isSpawned())
		{
			npc.setControllable(controllable);
		}
	}
	
	public Boolean isControllable()
	{
		if(isSpawned())
		{
			return npc.isControllable();
		}
		
		return null;
	}
	
	public void setFrozen(boolean frozen)
	{
		if(isSpawned())
		{
			npc.setFrozen(frozen);
		}
	}
	
	public Boolean isFrozen()
	{
		if(isSpawned())
		{
			return npc.isFrozen();
		}
		
		return null;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
		reset();
	}
	
	public String getSkin()
	{
		return skin;
	}
	
	public void setSkin(String skin)
	{
		this.skin = skin;
		reset();
	}
	
	public void reset()
	{
		if(isSpawned())
		{
			Location last = getLocation();
			despawn();
			spawn(last);
		}
	}
}

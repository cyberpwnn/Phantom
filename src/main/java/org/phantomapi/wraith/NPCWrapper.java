package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface NPCWrapper
{
	public void spawn(Location location);
	
	public void despawn();
	
	public boolean isSpawned();
	
	public Location getLocation();
	
	public void teleport(Location location);
	
	public Entity getTarget();
	
	public void setTarget(Entity entity);
	
	public LivingEntity getEntity();
	
	public int getEntityId();
	
	public void navigateTo(Location location);
	
	public void look(Location location);
	
	public void setPassenger(Entity entity);
	
	public Entity getPassenger();
	
	public void setYawPitch(float yaw, float pitch);
	
	public void setYaw(float yaw);
	
	public Float getYaw();
	
	public void setPitch(float pitch);
	
	public Float getPitch();
	
	public void setEquipment(WraithEquipment slot, ItemStack item);
	
	public ItemStack getEquipment(WraithEquipment slot);
	
	public void setInvulnerable(boolean invulnerable);
	
	public Boolean isInvulnerable();
	
	public void setCollision(boolean collision);
	
	public Boolean hasCollision();
	
	public void setControllable(boolean controllable);
	
	public Boolean isControllable();
	
	public void setFrozen(boolean frozen);
	
	public Boolean isFrozen();
	
	public String getName();
	
	public void setName(String name);
	
	public String getSkin();
	
	public void setSkin(String skin);
	
	public void reset();
}

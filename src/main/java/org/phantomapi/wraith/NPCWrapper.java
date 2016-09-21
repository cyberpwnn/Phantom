package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface NPCWrapper
{
	public void spawn(Location location);
	
	public void despawn();
	
	public boolean isSpawned();
	
	public Location getLocation();
	
	public void teleport(Location location);
	
	public Entity getEntity();
	
	public int getEntityId();
	
	public void navigateTo(Location location);
	
	public void look(Location location);
	
	public void setEquipment(WraithEquipment slot, ItemStack item);
	
	public ItemStack getEquipment(WraithEquipment slot);
	
	public String getName();
	
	public void setName(String name);
}

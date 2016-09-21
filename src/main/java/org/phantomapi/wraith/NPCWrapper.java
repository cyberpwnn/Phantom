package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.lang.GList;

public interface NPCWrapper
{
	public void spawn(Location location);
	
	public void despawn();
	
	public boolean isSpawned();
	
	public Location getLocation();
	
	public void teleport(Location location);
	
	public Entity getEntity();
	
	public int getEntityId();
	
	public void setTarget(Location location);
	
	public void setTarget(Entity entity);
	
	public void setTarget(WraithTarget target);
	
	public WraithTarget getTarget();
	
	public void tick();
	
	public void onTick();
	
	public void updateTarget();
	
	public void updateFocus();
	
	public void clearEquipment();
		
	public boolean hasFocus();
	
	public void lookAt(Location location);
	
	public void setFocus(Location location);
	
	public void setFocus(Entity entity);
	
	public void setFocus(WraithTarget target);
	
	public WraithTarget getFocus();
	
	public void setEquipment(WraithEquipment slot, ItemStack item);
	
	public ItemStack getEquipment(WraithEquipment slot);
	
	public String getName();
	
	public void setName(String name);
	
	public void setSneaking(boolean sneaking);
	
	public boolean isSneaking();
	
	public void setSprinting(boolean sprinting);
	
	public boolean isSprinting();
	
	public boolean isAllowedFlight();
	
	public void allowFlight(boolean flightFinding);
	
	public Player getPlayer();
	
	public void setProtected(boolean protect);
	
	public boolean isProtected();
	
	public boolean hasTarget();
	
	public void clearTarget();
	
	public void say(String message);
	
	public void say(String message, double radius);
	
	public void say(String message, Player p);
	
	public void say(String message, Player... players);
	
	public void say(String message, GList<Player> players);
	
	public String getChatName();
	
	public String getChatHover();
	
	public void setChatName(String name);
	
	public void setChatHover(String hover);
}

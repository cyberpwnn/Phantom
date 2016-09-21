package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class PhantomNPCWrapper implements NPCWrapper
{
	private NPC npc;
	
	public PhantomNPCWrapper(String name)
	{
		this.npc = null;
		this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
	}
	
	public void spawn(Location location)
	{
		npc.spawn(location);
	}
	
	public void despawn()
	{
		npc.despawn();
	}
	
	public boolean isSpawned()
	{
		return npc.isSpawned();
	}
	
	public Location getLocation()
	{
		return npc.getStoredLocation();
	}
	
	public void teleport(Location location)
	{
		if(isSpawned())
		{
			npc.teleport(location, TeleportCause.PLUGIN);
		}
	}
	
	public Entity getEntity()
	{
		if(isSpawned())
		{
			return npc.getEntity();
		}
		
		return null;
	}
	
	public int getEntityId()
	{
		if(isSpawned())
		{
			return getEntity().getEntityId();
		}
		
		return -1;
	}
	
	public void navigateTo(Location location)
	{
		if(isSpawned())
		{
			npc.getNavigator().setTarget(location);
		}
	}
	
	public void look(Location location)
	{
		if(isSpawned())
		{
			npc.faceLocation(location);
		}
	}
	
	public void setEquipment(WraithEquipment slot, ItemStack item)
	{
		if(isSpawned())
		{
			Player p = (Player) getEntity();
			
			switch(slot)
			{
				case CHEST:
					p.getInventory().setChestplate(item);
				case FEET:
					p.getInventory().setBoots(item);
				case HAND:
					p.getInventory().setItemInHand(item);
				case HEAD:
					p.getInventory().setHelmet(item);
				case LEGS:
					p.getInventory().setLeggings(item);
				default:
					break;
			}
		}
	}
	
	public ItemStack getEquipment(WraithEquipment slot)
	{
		if(isSpawned())
		{
			Player p = (Player) getEntity();
			
			switch(slot)
			{
				case CHEST:
					return p.getInventory().getChestplate();
				case FEET:
					return p.getInventory().getBoots();
				case HAND:
					return p.getInventory().getItemInHand();
				case HEAD:
					return p.getInventory().getHelmet();
				case LEGS:
					return p.getInventory().getLeggings();
				default:
					break;
			}
		}
		
		return null;
	}
	
	public String getName()
	{
		return npc.getName();
	}
	
	public void setName(String name)
	{
		npc.setName(name);
	}
}

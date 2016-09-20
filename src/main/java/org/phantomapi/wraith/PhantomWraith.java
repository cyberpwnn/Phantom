package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.phantomapi.lang.GList;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

/**
 * A Phantom wraith
 * 
 * @author cyberpwn
 */
public class PhantomWraith implements Wraith
{
	private EntityType type;
	private NPC npc;
	
	/**
	 * Create a wraith
	 * 
	 * @param type
	 *            the entity type
	 * @param name
	 *            the name
	 */
	public PhantomWraith(EntityType type, String name)
	{
		this.npc = CitizensAPI.getNPCRegistry().createNPC(type, name);
		
		if(npc.isSpawned())
		{
			this.type = npc.getEntity().getType();
		}
	}
	
	/**
	 * Create a phantom wraith wrapper from the entity id
	 * 
	 * @param id the id
	 */
	public PhantomWraith(int id)
	{
		this.npc = CitizensAPI.getNPCRegistry().getById(id);
		this.type = null;
		
		try
		{
			if(npc.isSpawned())
			{
				this.type = npc.getEntity().getType();
			}
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public String getName()
	{
		return npc.getName();
	}
	
	@Override
	public void setName(String name)
	{
		npc.setName(name);
	}
	
	@Override
	public EntityType getType()
	{
		if(npc.isSpawned())
		{
			this.type = npc.getEntity().getType();
		}
		
		return type;
	}
	
	@Override
	public void despawn()
	{
		npc.despawn();
	}
	
	@Override
	public void despawn(WraithDespawn despawnReason)
	{
		npc.despawn(DespawnReason.valueOf(despawnReason.toString()));
	}
	
	@Override
	public void spawn(Location location)
	{
		npc.spawn(location);
	}
	
	@Override
	public boolean isSpawned()
	{
		return npc.isSpawned();
	}
	
	@Override
	public Location getLocation()
	{
		return npc.getStoredLocation();
	}
	
	@Override
	public void lookAt(Location location)
	{
		npc.faceLocation(location);
	}
	
	@Override
	public void teleport(Location location)
	{
		teleport(location, TeleportCause.PLUGIN);
	}
	
	@Override
	public void teleport(Location location, TeleportCause cause)
	{
		npc.teleport(location, cause);
	}
	
	@Override
	public boolean isProtected()
	{
		return npc.isProtected();
	}
	
	@Override
	public void setProtected(boolean protect)
	{
		npc.setProtected(protect);
	}
	
	@Override
	public int getId()
	{
		return npc.getId();
	}
	
	@Override
	public Entity getEntity()
	{
		return npc.getEntity();
	}
	
	@Override
	public void destroy()
	{
		npc.destroy();
	}
	
	@Override
	public WraithNavigator getNavigator()
	{
		return new PhantomWraithNavigator(this, npc.getNavigator());
	}
	
	@Override
	public void addTrait(WrapperTrait trait)
	{
		npc.addTrait(trait);
	}
	
	@Override
	public GList<WrapperTrait> getTraits()
	{
		GList<WrapperTrait> traits = new GList<WrapperTrait>();
		
		for(Trait i : npc.getTraits())
		{
			traits.add((WrapperTrait) i);
		}
		
		return traits;
	}
	
	@Override
	public boolean hasTrait(Class<? extends WrapperTrait> trait)
	{
		return npc.hasTrait(trait);
	}
	
	@Override
	public void removeTrait(Class<? extends WrapperTrait> trait)
	{
		npc.removeTrait(trait);
	}
}

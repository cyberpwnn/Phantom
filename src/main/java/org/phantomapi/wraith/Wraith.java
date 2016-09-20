package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.phantomapi.lang.GList;

/**
 * Represents a wraith npc
 * 
 * @author cyberpwn
 */
public interface Wraith
{
	/**
	 * Get the npc name
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Set the name of this npc
	 * 
	 * @param name
	 *            the name
	 */
	public void setName(String name);
	
	/**
	 * Get the entity type
	 * 
	 * @return the entity type
	 */
	public EntityType getType();
	
	/**
	 * Despawn the npc
	 */
	public void despawn();
	
	/**
	 * Despawn the npc for the given reason
	 * 
	 * @param despawnReason
	 *            the reason
	 */
	public void despawn(WraithDespawn despawnReason);
	
	/**
	 * Spawn the entity
	 * 
	 * @param location
	 *            the location to spawn them at
	 */
	public void spawn(Location location);
	
	/**
	 * Check if the npc is already spawned in and not despawned
	 * 
	 * @return
	 */
	public boolean isSpawned();
	
	/**
	 * Get the location of the npc, or the last known location if it is not
	 * spawned
	 * 
	 * @return the location or last known location from last spawn or null if
	 *         never spawned
	 */
	public Location getLocation();
	
	/**
	 * Look at the given location
	 * 
	 * @param location
	 *            the location for the npc to look at
	 */
	public void lookAt(Location location);
	
	/**
	 * Teleport to the given location
	 * 
	 * @param location
	 *            the location
	 */
	public void teleport(Location location);
	
	/**
	 * Teleport to the given location with the given cause
	 * 
	 * @param location
	 *            the location
	 * @param cause
	 *            the reason
	 */
	public void teleport(Location location, TeleportCause cause);
	
	/**
	 * Is the entity protected from outside interaction that would damage move
	 * or change the wraith's state
	 * 
	 * @return true if the npc is able to be changed from players and the world
	 */
	public boolean isProtected();
	
	/**
	 * Set the wraith's protection status.
	 * 
	 * @param protect
	 *            if set to true, the npc will not take damage or be moved by
	 *            outside physical forces
	 */
	public void setProtected(boolean protect);
	
	/**
	 * Get the wraith's id
	 * 
	 * @return the id
	 */
	public int getId();
	
	/**
	 * Get the entity of this wraith
	 * 
	 * @return the bukkit entity
	 */
	public Entity getEntity();
	
	/**
	 * Destroy the entity and all of it's memory and data
	 */
	public void destroy();
	
	/**
	 * Get the wraith navigator
	 * 
	 * @return the navigator
	 */
	public WraithNavigator getNavigator();
	
	/**
	 * Add a wraith trait
	 * 
	 * @param trait
	 *            the trait
	 */
	public void addTrait(WraithTrait trait);
	
	/**
	 * Get all wraith traits
	 * 
	 * @return the traits
	 */
	public GList<WraithTrait> getTraits();
	
	/**
	 * Does this wraith have the given trait
	 * 
	 * @param trait
	 *            the trait class
	 * @return true if it does
	 */
	public boolean hasTrait(Class<? extends WraithTrait> trait);
	
	/**
	 * Remove the given trait from this wraith
	 * 
	 * @param trait
	 *            the trait class
	 */
	public void removeTrait(Class<? extends WraithTrait> trait);
}

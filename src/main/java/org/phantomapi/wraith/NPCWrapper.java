package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.lang.GList;

/**
 * NPC wrapper
 * 
 * @author cyberpwn
 */
public interface NPCWrapper
{
	/**
	 * Spawn the npc at the given location. NOTE, you must call this before
	 * using most methods
	 * 
	 * @param location
	 *            the given location
	 */
	public void spawn(Location location);
	
	/**
	 * Despawn the given npc. Ensure to destroy it if you do not plan on
	 * re-spawning it
	 */
	public void despawn();
	
	/**
	 * Is the entity spawned?
	 * 
	 * @return true if the entity is spawned
	 */
	public boolean isSpawned();
	
	/**
	 * Get the npc's location
	 * 
	 * @return the location of the npc
	 */
	public Location getLocation();
	
	/**
	 * Teleport the entity to the given location
	 * 
	 * @param location
	 *            the given location
	 */
	public void teleport(Location location);
	
	/**
	 * Get the underlying entity for this npc
	 * 
	 * @return the entity (it's a player)
	 */
	public Entity getEntity();
	
	/**
	 * Get the underlying entity id for this npc
	 * 
	 * @return the entity id
	 */
	public int getEntityId();
	
	/**
	 * Set the target to a location
	 * 
	 * @param location
	 *            the location target
	 */
	public void setTarget(Location location);
	
	/**
	 * Set the target to an entity, will activley re-calc pathfinding
	 * 
	 * @param entity
	 *            the entity to target
	 */
	public void setTarget(Entity entity);
	
	/**
	 * Set the target to a wraith target (entity or location)
	 * 
	 * @param target
	 *            the wraith target
	 */
	public void setTarget(WraithTarget target);
	
	/**
	 * Get the entity target
	 * 
	 * @return the wraith target
	 */
	public WraithTarget getTarget();
	
	/**
	 * Tick this npc (automatically ticked)
	 */
	public void tick();
	
	/**
	 * Called when the entity has been ticked
	 */
	public void onTick();
	
	/**
	 * Updates the target for this npc (automatically updated)
	 */
	public void updateTarget();
	
	/**
	 * Updates the npc's focus (automatic)
	 */
	public void updateFocus();
	
	/**
	 * Destroy the npc
	 */
	public void destroy();
	
	/**
	 * Remove all equipment from this npc
	 */
	public void clearEquipment();
	
	/**
	 * Does this npc have a focus target?
	 * 
	 * @return true if it does
	 */
	public boolean hasFocus();
	
	/**
	 * Make this npc look at a certain location
	 * 
	 * @param location
	 *            the given location
	 */
	public void lookAt(Location location);
	
	/**
	 * Set the npc to focus (keep looking at) the given target
	 * 
	 * @param location
	 *            the target
	 */
	public void setFocus(Location location);
	
	/**
	 * Set the npc to focus (keep looking at) the given target
	 * 
	 * @param entity
	 *            the target
	 */
	public void setFocus(Entity entity);
	
	/**
	 * Set the npc to focus (keep looking at) the given target
	 * 
	 * @param target
	 *            the target
	 */
	public void setFocus(WraithTarget target);
	
	/**
	 * Get the focus target for this npc
	 * 
	 * @return the target or null
	 */
	public WraithTarget getFocus();
	
	/**
	 * Set the equipment slot for this npc
	 * 
	 * @param slot
	 *            the equipment slot
	 * @param item
	 *            the item stack
	 */
	public void setEquipment(WraithEquipment slot, ItemStack item);
	
	/**
	 * Get the item stack for the given equipment slot
	 * 
	 * @param slot
	 *            the equipment slot
	 * @return the item stack or null
	 */
	public ItemStack getEquipment(WraithEquipment slot);
	
	/**
	 * Get the name of this npc
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Change the name of this npc
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name);
	
	/**
	 * Set this entity to sneak or not
	 * 
	 * @param sneaking
	 *            the value true for sneaking
	 */
	public void setSneaking(boolean sneaking);
	
	/**
	 * Is the npc currently sneaking?
	 * 
	 * @return true if it is
	 */
	public boolean isSneaking();
	
	/**
	 * Set the npc to sprint or not
	 * 
	 * @param sprinting
	 *            true for sprinting
	 */
	public void setSprinting(boolean sprinting);
	
	/**
	 * Is the current npc sprinting?
	 * 
	 * @return true if it is
	 */
	public boolean isSprinting();
	
	/**
	 * Is this npc allowed to fly when pathfinding is impossible
	 * 
	 * @return true if it is
	 */
	public boolean isAllowedFlight();
	
	/**
	 * Change this npc to be allowed or not to fly when pathfinding is
	 * impossible
	 * 
	 * @param flightFinding
	 *            true to fly when absolutley needed
	 */
	public void allowFlight(boolean flightFinding);
	
	/**
	 * Get the underlying player object for this npc. Be careful
	 * 
	 * @return the player object
	 */
	public Player getPlayer();
	
	/**
	 * Set this entity as protected
	 * 
	 * @param protect
	 *            if true, this entity cannot be damaged or killed
	 */
	public void setProtected(boolean protect);
	
	/**
	 * Is this entity protected?
	 * 
	 * @return true if it is
	 */
	public boolean isProtected();
	
	/**
	 * Does this entity have a target?
	 * 
	 * @return true if it does
	 */
	public boolean hasTarget();
	
	/**
	 * Clear the npc's target and stop pathfinding
	 */
	public void clearTarget();
	
	/**
	 * Send a message to every player with the tag builder format
	 * 
	 * @param message
	 *            the message
	 */
	public void say(String message);
	
	/**
	 * Send a message to all players within the radius
	 * 
	 * @param message
	 *            the message
	 * @param radius
	 *            the radius
	 */
	public void say(String message, double radius);
	
	/**
	 * Send a message to the given player
	 * 
	 * @param message
	 *            the message
	 * @param p
	 *            the player
	 */
	public void say(String message, Player p);
	
	/**
	 * Send a message to the given players
	 * 
	 * @param message
	 *            the message
	 * @param players
	 *            the players
	 */
	public void say(String message, Player... players);
	
	/**
	 * Send a message to the given players
	 * 
	 * @param message
	 *            the message
	 * @param players
	 *            the players
	 */
	public void say(String message, GList<Player> players);
	
	/**
	 * Get the chat name for this npc
	 * 
	 * @return the chatting name
	 */
	public String getChatName();
	
	/**
	 * Get the hover text for this chat name
	 * 
	 * @return the hover text
	 */
	public String getChatHover();
	
	/**
	 * Set the npc to be aggressive to any of it's targets
	 * 
	 * @param aggro
	 *            true to attack the given target
	 */
	public void setAggressive(boolean aggro);
	
	/**
	 * Is the npc aggressive to it's targets?
	 * 
	 * @return true if it is
	 */
	public boolean isAggressive();
	
	/**
	 * Set the chat name for this npc
	 * 
	 * @param name
	 *            the chat name
	 */
	public void setChatName(String name);
	
	/**
	 * Set the chat hover name for this npc
	 * 
	 * @param hover
	 *            the hover
	 */
	public void setChatHover(String hover);
	
	/**
	 * Fired when a player interacts with this npc
	 * 
	 * @param p
	 *            the interacting player
	 */
	public void onInteract(Player p);
	
	/**
	 * Fired when an entity collides with this npc
	 * 
	 * @param entity
	 *            the colliding entity
	 */
	public void onCollide(Entity entity);
	
	/**
	 * Fired when this entity is damaged
	 * 
	 * @param damager
	 *            the damaging entity
	 * @param damage
	 *            the damage amount
	 */
	public void onDamage(Entity damager, double damage);
}

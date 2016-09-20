package org.phantomapi.wraith;

/**
 * Despawn reason for wraith npcs
 * 
 * @author cyberpwn
 */
public enum WraithDespawn
{
	/**
	 * Wraith despawned because of a chunk unload
	 */
	CHUNK_UNLOAD,
	
	/**
	 * Wraith despawned because of death
	 */
	DEATH,
	
	/**
	 * Wraith despawned because of an upcoming respawn
	 */
	PENDING_RESPAWN,
	
	/**
	 * Wraith despawned because of a plugin
	 */
	PLUGIN,
	
	/**
	 * Wraith despawned because it was removed
	 */
	REMOVAL,
	
	/**
	 * Wraith despawned because the world unloaded
	 */
	WORLD_UNLOAD;
}

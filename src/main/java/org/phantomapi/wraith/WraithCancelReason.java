package org.phantomapi.wraith;

/**
 * Represents a cancel reason for a wraith to stop pathfinding
 * 
 * @author cyberpwn
 */
public enum WraithCancelReason
{
	/**
	 * Cancelled due to despawning
	 */
	NPC_DESPAWNED,
	
	/**
	 * Cancelled due to plugin
	 */
	PLUGIN,
	
	/**
	 * Cancelled due to a replace
	 */
	REPLACE,
	
	/**
	 * Cancelled due to getting stuck
	 */
	STUCK,
	
	/**
	 * Cancelled due to death
	 */
	TARGET_DIED,
	
	/**
	 * Cancelled due to changing worlds
	 */
	TARGET_MOVED_WORLD;
}

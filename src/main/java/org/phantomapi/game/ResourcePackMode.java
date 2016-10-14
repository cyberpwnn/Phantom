package org.phantomapi.game;

/**
 * Resource pack mode for joining
 * 
 * @author cyberpwn
 */
public enum ResourcePackMode
{
	/**
	 * Do not offer a resource pack
	 */
	NONE,
	
	/**
	 * Offer the resource pack
	 */
	OFFER,
	
	/**
	 * Force the resource pack or kick the player if they decline/fail to
	 * load/don't answer
	 */
	FORCE;
}

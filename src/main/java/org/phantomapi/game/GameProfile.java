package org.phantomapi.game;

/**
 * A Game profile for handling basic game configurations
 * 
 * @author cyberpwn
 */
public class GameProfile
{
	private final Game game;
	private final ResourcePackProfile resourceProfile;
	private final CapacityProfile capacityProfile;
	
	public GameProfile(Game game)
	{
		this.game = game;
		this.resourceProfile = new ResourcePackProfile();
		this.capacityProfile = new CapacityProfile();
	}
	
	/**
	 * Get the game
	 * 
	 * @return the game
	 */
	public Game getGame()
	{
		return game;
	}
	
	/**
	 * Get the resource profile for resource pack profiling
	 * 
	 * @return the profiler
	 */
	public ResourcePackProfile getResourceProfile()
	{
		return resourceProfile;
	}
	
	/**
	 * Get the capacity profile for handling player limits
	 * 
	 * @return the capacity profile
	 */
	public CapacityProfile getCapacityProfile()
	{
		return capacityProfile;
	}
}

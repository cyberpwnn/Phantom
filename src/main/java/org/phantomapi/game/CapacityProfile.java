package org.phantomapi.game;

/**
 * Game capacity profile
 * 
 * @author cyberpwn
 */
public class CapacityProfile
{
	private int playerLimit;
	private CapacityMode mode;
	
	public CapacityProfile()
	{
		this.playerLimit = -1;
		this.mode = CapacityMode.UNLIMITED;
	}
	
	public int getPlayerLimit()
	{
		return playerLimit;
	}
	
	public void setPlayerLimit(int playerLimit)
	{
		this.playerLimit = playerLimit;
	}
	
	public CapacityMode getMode()
	{
		return mode;
	}
	
	public void setMode(CapacityMode mode)
	{
		this.mode = mode;
	}
	
	/**
	 * Could another player join with the given count?
	 * 
	 * @param count
	 *            the current count
	 * @return true if another player can join
	 */
	public boolean canJoin(int count)
	{
		return mode.equals(CapacityMode.UNLIMITED) || playerLimit >= count + 1;
	}
}

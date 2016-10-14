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
	private boolean acceptingPlayers;
	
	public CapacityProfile()
	{
		this.playerLimit = -1;
		this.mode = CapacityMode.UNLIMITED;
		this.acceptingPlayers = true;
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

	public boolean isAcceptingPlayers()
	{
		return acceptingPlayers;
	}

	public void setAcceptingPlayers(boolean acceptingPlayers)
	{
		this.acceptingPlayers = acceptingPlayers;
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
		return (mode.equals(CapacityMode.UNLIMITED) || playerLimit >= count + 1) && acceptingPlayers;
	}
}

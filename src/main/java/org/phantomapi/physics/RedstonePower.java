package org.phantomapi.physics;

/**
 * Represents redstone power
 * 
 * @author cyberpwn
 */
public class RedstonePower
{
	private byte power;
	
	/**
	 * Create redstone power
	 * 
	 * @param power
	 *            the power level from 0-15
	 */
	public RedstonePower(byte power)
	{
		this.power = power;
	}
	
	/**
	 * Get the redstone power
	 * 
	 * @return the held power
	 */
	public byte getPower()
	{
		if(power > 15)
		{
			return 15;
		}
		
		if(power < 0)
		{
			return 0;
		}
		
		return power;
	}
	
	/**
	 * Set the redstone power
	 * 
	 * @param power
	 *            the given power between 0 and 15
	 */
	public void setPower(byte power)
	{
		this.power = power;
	}
}

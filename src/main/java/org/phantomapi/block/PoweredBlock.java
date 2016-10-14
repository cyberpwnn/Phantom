package org.phantomapi.block;

import org.phantomapi.physics.RedstonePower;

/**
 * Represents a powerable block
 * 
 * @author cyberpwn
 */
public interface PoweredBlock
{
	/**
	 * Get the power object
	 * 
	 * @return the power object
	 */
	public RedstonePower getPower();
	
	/**
	 * Set the redstone power
	 * 
	 * @param power
	 *            the power
	 */
	public void setPower(RedstonePower power);
}

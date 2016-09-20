package org.phantomapi.wraith;

import org.bukkit.Location;
import net.citizensnpcs.npc.ai.PathStrategy;

/**
 * Wraith path strategy
 * 
 * @author cyberpwn
 */
public class WraithPathStrategy
{
	private PathStrategy strat;
	
	/**
	 * Create a path Strategy from a PathStrategy object
	 * 
	 * @param base
	 *            the base object
	 */
	public WraithPathStrategy(Object base)
	{
		strat = (PathStrategy) base;
	}
	
	/**
	 * Resume pathfinding
	 */
	public void resume()
	{
		strat.clearCancelReason();
	}
	
	/**
	 * Get the reason the pathfinding stopped
	 * 
	 * @return the wraith cancel reason
	 */
	public WraithCancelReason getCancelReason()
	{
		return WraithCancelReason.valueOf(strat.getCancelReason().toString());
	}
	
	/**
	 * Get the destination for the pathfinding
	 * 
	 * @return the destination
	 */
	public Location getDestination()
	{
		return strat.getTargetAsLocation();
	}
	
	/**
	 * Stop pathfinding
	 */
	public void stop()
	{
		strat.stop();
	}
	
	/**
	 * Update the pathfind target
	 */
	public void update()
	{
		strat.update();
	}
}

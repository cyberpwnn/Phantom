package org.phantomapi.wraith;

import org.bukkit.entity.Player;
import org.phantomapi.util.M;
import org.phantomapi.world.Area;

/**
 * Focus ai controller. Focus on the closest player in the given range
 * 
 * @author cyberpwn
 */
public abstract class FocusAIController extends WraithHandle
{
	private Double radius;
	
	public FocusAIController(Wraith wraith, Double radius)
	{
		super(wraith);
	}
	
	@Override
	public void onTick()
	{
		if(M.r(0.2))
		{
			Area a = new Area(getWraith().getLocation(), radius);
			
			Player f = null;
			Double dist = Double.MAX_VALUE;
			
			for(Player i : a.getNearbyPlayers())
			{
				if(i.getLocation().distanceSquared(getWraith().getLocation()) < dist)
				{
					f = i;
					dist = i.getLocation().distanceSquared(getWraith().getLocation());
				}
			}
			
			if(getWraith().getFocus().getTarget().equals(f))
			{
				return;
			}
			
			else
			{
				getWraith().setFocus(f);
				onFocusChanged(f);
			}
		}
	}
	
	@Override
	public void onUnbind()
	{
		
	}
	
	@Override
	public void onBind()
	{
		
	}
	
	public abstract void onFocusChanged(Player player);
}

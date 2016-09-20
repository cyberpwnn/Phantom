package org.phantomapi.npc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import net.citizensnpcs.api.ai.Navigator;

public class PhantomWraithNavigator implements WraithNavigator
{
	private Navigator navigator;
	private Wraith wraith;
	
	public PhantomWraithNavigator(Wraith wraith, Object navBase)
	{
		this.navigator = (Navigator) navBase;
	}
	
	@Override
	public void cancel()
	{
		navigator.cancelNavigation();
	}
	
	@Override
	public WraithTarget getTarget()
	{
		return new WraithTarget(navigator.getEntityTarget().getTarget(), navigator.getEntityTarget().isAggressive());
	}
	
	@Override
	public Wraith getWraith()
	{
		return wraith;
	}
	
	@Override
	public WraithTargetType getTargetType()
	{
		return WraithTargetType.valueOf(navigator.getTargetType().toString());
	}
	
	@Override
	public boolean isNavigating()
	{
		return navigator.isNavigating();
	}
	
	@Override
	public boolean isPaused()
	{
		return navigator.isPaused();
	}
	
	@Override
	public void setPaused(boolean paused)
	{
		navigator.setPaused(paused);
	}
	
	@Override
	public void setTarget(Entity entity, boolean aggressive)
	{
		navigator.setTarget(entity, aggressive);
	}
	
	@Override
	public void setTarget(Location target)
	{
		navigator.setTarget(target);
	}
}

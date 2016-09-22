package org.phantomapi.wraith;

import org.bukkit.entity.Player;

/**
 * GUI AI Controller, calls onInterfaceLaunch when a player interacts with this
 * entity
 * 
 * @author cyberpwn
 */
public abstract class GUIAIController extends WraithHandle implements InteractiveHandle
{
	public GUIAIController(Wraith wraith)
	{
		super(wraith);
	}
	
	@Override
	public void onInteract(Player p)
	{
		onInterfaceLaunch(getWraith(), p);
	}
	
	public abstract void onInterfaceLaunch(Wraith wraith, Player player);
}

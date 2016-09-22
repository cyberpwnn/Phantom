package org.phantomapi.wraith;

import org.bukkit.entity.Player;

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

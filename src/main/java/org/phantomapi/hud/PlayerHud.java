package org.phantomapi.hud;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.phantomapi.physics.VectorMath;
import org.phantomapi.util.P;

public abstract class PlayerHud extends BaseHud
{
	private boolean closeOnMove;
	private Location last;
	
	public PlayerHud(Player player, boolean closeOnMove)
	{
		super(player);
		
		this.closeOnMove = closeOnMove;
		last = null;
	}
	
	public PlayerHud(Player player)
	{
		this(player, true);
	}
	
	@Override
	public Location getBaseLocation()
	{
		Location host = P.getHand(player, 0f, 0f).clone().add(0, -3, 0).add(player.getLocation().getDirection().clone().multiply(2));
		Vector left = VectorMath.angleLeft(player.getLocation().getDirection(), 90).clone().multiply(index);
		
		return host.clone().add(left);
	}
	
	@Override
	public void onUpdateInternal()
	{
		holo.setLocation(getBaseLocation());
		
		if(closeOnMove)
		{
			if(last != null)
			{
				if(!player.getLocation().getBlock().getLocation().equals(last.getBlock().getLocation()))
				{
					close();
				}
			}
			
			last = player.getLocation();
		}
	}
}
